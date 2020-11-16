package ripple.core;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractNode {
    private int id;
    private String type;
    private List<NodeMetadata> nodeList;

    private String address;
    private int port;
    private Server server;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Server getServer() {
        return server;
    }

    private void setServer(Server server) {
        this.server = server;
    }

    public abstract void registerHandlers(ServletContextHandler servletContextHandler);


    public AbstractNode(int id, String type) {
        this(id, type, 0);
    }

    public AbstractNode(int id, String type, int port) {
        this.setId(id);
        this.setType(type);
        this.setNodeList(new ArrayList<>());
        this.setPort(port);
        this.initialize();
    }

    private void initialize() {
        try {
            this.setServer(new Server());
            ServerConnector serverConnector = new ServerConnector(this.getServer());
            serverConnector.setPort(this.getPort());
            this.getServer().setConnectors(new Connector[]{serverConnector});

            ServletContextHandler servletContextHandler = new ServletContextHandler();

            this.registerHandlers(servletContextHandler);

            this.getServer().setHandler(servletContextHandler);
            this.getServer().start();
            this.setAddress(InetAddress.getLocalHost().getHostAddress());
            this.setPort(serverConnector.getLocalPort());
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
