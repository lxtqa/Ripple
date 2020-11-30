package ripple.server.simulation.core.tracing.brave.servlet;

import brave.Span;
import brave.SpanCustomizer;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class TracingFilter implements Filter {
    static final Propagation.Getter<HttpServletRequest, String> GETTER =
            new Propagation.Getter<HttpServletRequest, String>() {
                @Override public String get(HttpServletRequest carrier, String key) {
                    return carrier.getHeader(key);
                }

                @Override public String toString() {
                    return "HttpServletRequest::getHeader";
                }
            };
    static final HttpServletAdapter ADAPTER = new HttpServletAdapter();

    public static Filter create(Tracing tracing) {
        return new TracingFilter(HttpTracing.create(tracing));
    }

    public static Filter create(HttpTracing httpTracing) {
        return new TracingFilter(httpTracing);
    }

    final ServletRuntime servlet = ServletRuntime.get();
    final CurrentTraceContext currentTraceContext;
    final Tracer tracer;
    final HttpServerHandler<HttpServletRequest, HttpServletResponse> handler;
    final TraceContext.Extractor<HttpServletRequest> extractor;

    TracingFilter(HttpTracing httpTracing) {
        tracer = httpTracing.tracing().tracer();
        currentTraceContext = httpTracing.tracing().currentTraceContext();
        handler = HttpServerHandler.create(httpTracing, ADAPTER);
        extractor = httpTracing.tracing().propagation().extractor(GETTER);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = servlet.httpResponse(response);

        // Prevent duplicate spans for the same request
        TraceContext context = (TraceContext) request.getAttribute(TraceContext.class.getName());
        if (context != null) {
            // A forwarded request might end up on another thread, so make sure it is scoped
            CurrentTraceContext.Scope scope = currentTraceContext.maybeScope(context);
            try {
                chain.doFilter(request, response);
            } finally {
                scope.close();
            }
            return;
        }

        Span span = handler.handleReceive(extractor, httpRequest);

        // Add attributes for explicit access to customization or span context
        request.setAttribute(SpanCustomizer.class.getName(), span.customizer());
        request.setAttribute(TraceContext.class.getName(), span.context());

        Throwable error = null;
        CurrentTraceContext.Scope scope = currentTraceContext.newScope(span.context());
        try {
            // any downstream code can see Tracer.currentSpan() or use Tracer.currentSpanCustomizer()
            chain.doFilter(httpRequest, httpResponse);
        } catch (IOException | ServletException | RuntimeException | Error e) {
            error = e;
            throw e;
        } finally {
            scope.close();
            if (servlet.isAsync(httpRequest)) { // we don't have the actual response, handle later
                servlet.handleAsync(handler, httpRequest, httpResponse, span);
            } else { // we have a synchronous response, so we can finish the span
                handler.handleSend(ADAPTER.adaptResponse(httpRequest, httpResponse), error, span);
            }
        }
    }

    @Override public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
}

