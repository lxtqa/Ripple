package ripple.agent.core.node;

import ripple.agent.Main;
import ripple.agent.core.Cluster;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public abstract class AbstractNode {
    private NodeMetadata metadata;
    private Server server;
    private Cluster cluster;

    public NodeMetadata getMetadata() {
        return metadata;
    }

    private void setMetadata(NodeMetadata metadata) {
        this.metadata = metadata;
    }

    public Server getServer() {
        return server;
    }

    private void setServer(Server server) {
        this.server = server;
    }

    public Cluster getCluster() {
        return cluster;
    }

    private void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    /**
     * 注册处理程序，实现节点仿真
     *
     * @param servletContextHandler
     */
    public abstract void registerHandlers(ServletContextHandler servletContextHandler);

    public AbstractNode(UUID clusterUuid, String type, Cluster cluster) {
        UUID uuid = UUID.randomUUID();
        this.setMetadata(new NodeMetadata());
        this.getMetadata().setUuid(uuid);
        this.getMetadata().setAgentUuid(Main.AGENT_UUID);
        this.getMetadata().setClusterUuid(clusterUuid);
        this.getMetadata().setType(type);

        this.setCluster(cluster);
        this.initialize();
    }

    private void initialize() {
        try {
            this.setServer(new Server());
            ServerConnector serverConnector = new ServerConnector(this.getServer());
            serverConnector.setPort(0);
            this.getServer().setConnectors(new Connector[]{serverConnector});

            ServletContextHandler servletContextHandler = new ServletContextHandler();

            // 注册每类节点特定的ServletHandler
            this.registerHandlers(servletContextHandler);

            this.getServer().setHandler(servletContextHandler);
            this.getServer().start();
            this.getMetadata().setAddress(InetAddress.getLocalHost().getHostAddress());
            this.getMetadata().setPort(serverConnector.getLocalPort());
        } catch (Exception exception) {
            exception.printStackTrace();
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

}
