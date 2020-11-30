package ripple.server.simulation.core.tracing;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import ripple.server.simulation.core.tracing.brave.servlet.TracingFilter;
import ripple.server.simulation.utils.Constants;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.EnumSet;

/**
 * @author songao
 */
public class TracingServletContextListener implements ServletContextListener {
    private Sender sender = OkHttpSender.create(System.getProperty("tracing.zipkin.url", Constants.ZIPKIN_DEFAULT_URL));
    private AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);
    private Tracing tracing;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // UUID的前5位 作为tracing的service name
        String nodeUUIDPrefix =
                (servletContextEvent.getServletContext().getAttribute("node.UUID"))
                .toString().substring(0,5);
        tracing = Tracing.newBuilder()
                .localServiceName("nacos-node-" + nodeUUIDPrefix)
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                        .build()
                )
                .spanReporter(spanReporter).build();

        servletContextEvent.getServletContext().setAttribute("TRACING",tracing);
        servletContextEvent
                .getServletContext()
                .addFilter("tracingFilter", TracingFilter.create(tracing))
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            tracing.close(); // disables Tracing.current()
            spanReporter.close(); // stops reporting thread and flushes data
            sender.close(); // closes any transport resources
        } catch (IOException e) {
            // do something real
        }
    }
}
