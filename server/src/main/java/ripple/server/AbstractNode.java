package ripple.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractNode {
    private int id;
    private String type;
    private List<NodeMetadata> nodeList;
    private ConcurrentHashMap<String, String> storage;
    private ConcurrentHashMap<String, List<ClientMetadata>> subscription;

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

    public ConcurrentHashMap<String, String> getStorage() {
        return storage;
    }

    public void setStorage(ConcurrentHashMap<String, String> storage) {
        this.storage = storage;
    }

    public ConcurrentHashMap<String, List<ClientMetadata>> getSubscription() {
        return subscription;
    }

    public void setSubscription(ConcurrentHashMap<String, List<ClientMetadata>> subscription) {
        this.subscription = subscription;
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
        this.setStorage(new ConcurrentHashMap<>());
        this.setSubscription(new ConcurrentHashMap<>());
        this.setPort(port);
    }

    public void start() {
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

    public synchronized void subscribe(String callbackAddress, int callbackPort, String key) {
        if (this.getSubscription().get(key) == null) {
            this.getSubscription().put(key, new ArrayList<>());
        }
        List<ClientMetadata> subscribers = this.getSubscription().get(key);
        for (ClientMetadata metadata : subscribers) {
            if (metadata.getAddress().equals(callbackAddress) && metadata.getPort() == callbackPort) {
                return;
            }
        }
        ClientMetadata clientMetadata = new ClientMetadata();
        clientMetadata.setAddress(callbackAddress);
        clientMetadata.setPort(callbackPort);
        subscribers.add(clientMetadata);
    }

    public synchronized void unsubscribe() {
        // TODO
    }
}
