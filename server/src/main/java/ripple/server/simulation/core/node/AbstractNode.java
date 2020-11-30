package ripple.server.simulation.core.node;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.tracing.TracingServletContextListener;
import ripple.server.simulation.helper.RegistryHelper;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 * @author songao
 */
public abstract class AbstractNode {
    private UUID uuid;
    private String address;
    private int port;
    private Server server;
    private Emulator emulator;
    private Context context;
    protected GlobalExecutor executor = new GlobalExecutor();

    /**
     * 每个节点维护的最新集群列表
     */
    protected List<AbstractPeer> localPeerList;

    public RegistryHelper registryHelper = new RegistryHelper();

    public UUID getUuid() {
        return uuid;
    }

    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    public Server getServer() {
        return server;
    }

    private void setServer(Server server) {
        this.server = server;
    }

    public Emulator getEmulator() {
        return emulator;
    }

    private void setEmulator(Emulator emulator) {
        this.emulator = emulator;
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    /**
     * 注册处理程序，实现节点仿真
     * @param servletContextHandler
     */
    public abstract void registerHandlers(ServletContextHandler servletContextHandler);

    public AbstractNode(Emulator emulator, Context context) {
        this.setEmulator(emulator);
        this.setContext(context);
        this.initialize();
    }

    private void addTracingHandler(ServletContextHandler handler) {
        handler.setContextPath("/");

        ServletContextListener listener = new TracingServletContextListener();
        handler.addEventListener(listener);
    }

    private void initialize() {
        try {
            this.setUuid(UUID.randomUUID());
            this.setServer(new Server());
            ServerConnector serverConnector = new ServerConnector(this.getServer());
            serverConnector.setPort(0);
            this.getServer().setConnectors(new Connector[]{serverConnector});
            HandlerCollection handlerCollection = new HandlerCollection();

            ServletContextHandler servletContextHandler = new ServletContextHandler();
            servletContextHandler.setAttribute("node.UUID",this.getUuid());

            // 添加tracing相关的ServletContextHandler
            addTracingHandler(servletContextHandler);

            // ServletHandler baseServletHandler = new ServletHandler();
            // baseServletHandler.addServletWithMapping(AbstractNodeServlet.class, "/*");
            // handlerCollection.addHandler(baseServletHandler);

            // 注册每类节点特定的ServletHandler
            this.registerHandlers(servletContextHandler);

            this.getServer().setHandler(servletContextHandler);
            this.getServer().start();
            this.setPort(serverConnector.getLocalPort());
            this.setAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 节点基本属性初始化完毕后，启动节点动作，例如获取集群列表
     */
    protected abstract void start();

    @SuppressWarnings("serial")
    public static class AbstractNodeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("<h1>Hello!</h1>");
        }
    }

    public boolean stop() {
        try {
            this.getServer().stop();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public class GetClusterAddress implements Runnable {

        @Override
        public void run() {
            initPeers();
        }


    }

    /**
     *  从engine获取最新集群节点列表
     */
    public abstract void initPeers();

}
