package ripple.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.entity.Item;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNode.class);

    private int id;
    private String type;
    private List<NodeMetadata> nodeList;
    private ConcurrentHashMap<String, Item> storage;
    private ConcurrentHashMap<String, List<ClientMetadata>> subscription;

    private String address;
    private int port;
    private Server server;
    private boolean running;

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

    public ConcurrentHashMap<String, Item> getStorage() {
        return storage;
    }

    public void setStorage(ConcurrentHashMap<String, Item> storage) {
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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

    public synchronized boolean start() {
        if (this.isRunning()) {
            return true;
        }
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
            this.setRunning(true);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized boolean stop() {
        if (!this.isRunning()) {
            return true;
        }
        try {
            this.getServer().stop();
            this.setRunning(false);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized void subscribe(String callbackAddress, int callbackPort, String key) {
        LOGGER.info("[AbstractNode] subscribe() called: Callback Address = "
                + callbackAddress + "; Callback Port = " + callbackPort + "; Key = " + key + ".");
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

    public synchronized void unsubscribe(String callbackAddress, int callbackPort, String key) {
        LOGGER.info("[AbstractNode] unsubscribe() called: Callback Address = "
                + callbackAddress + "; Callback Port = " + callbackPort + "; Key = " + key + ".");
        if (this.getSubscription().get(key) == null) {
            return;
        }
        List<ClientMetadata> subscribers = this.getSubscription().get(key);
        ClientMetadata toRemove = null;
        for (ClientMetadata metadata : subscribers) {
            if (metadata.getAddress().equals(callbackAddress) && metadata.getPort() == callbackPort) {
                toRemove = metadata;
                break;
            }
        }
        if (toRemove != null) {
            subscribers.remove(toRemove);
        }
    }
}
