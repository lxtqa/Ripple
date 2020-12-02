package ripple.server.core;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.helper.Storage;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public abstract class AbstractNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNode.class);

    private int id;
    private String type;
    private Storage storage;
    private Set<NodeMetadata> nodeList;
    private ConcurrentHashMap<ItemKey, Set<ClientMetadata>> subscription;
    private Set<ClientMetadata> connectedClients;

    private String address;
    private int port;
    private Server server;
    private boolean running;

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    protected Storage getStorage() {
        return storage;
    }

    protected void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Set<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(Set<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    public ConcurrentHashMap<ItemKey, Set<ClientMetadata>> getSubscription() {
        return subscription;
    }

    public void setSubscription(ConcurrentHashMap<ItemKey, Set<ClientMetadata>> subscription) {
        this.subscription = subscription;
    }

    public Set<ClientMetadata> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(Set<ClientMetadata> connectedClients) {
        this.connectedClients = connectedClients;
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

    public abstract Item get(String applicationName, String key);

    public abstract List<Item> getAll();

    public abstract boolean put(String applicationName, String key, String value);

    public abstract boolean delete(String applicationName, String key);


    public AbstractNode(int id, String type, String storageLocation) {
        this(id, type, storageLocation, 0);
    }

    public AbstractNode(int id, String type, String storageLocation, int port) {
        this.setId(id);
        this.setType(type);
        this.setNodeList(new HashSet<>());
        this.setStorage(new Storage(storageLocation));
        this.setSubscription(new ConcurrentHashMap<>());
        this.setPort(port);
        this.setConnectedClients(new HashSet<>());
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

    public synchronized void subscribe(String callbackAddress, int callbackPort, String applicationName, String key) {
        LOGGER.info("[AbstractNode] subscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , callbackAddress, callbackPort, applicationName, key);
        ItemKey itemKey = new ItemKey(applicationName, key);
        if (this.getSubscription().get(itemKey) == null) {
            this.getSubscription().put(itemKey, new HashSet<>());
        }
        Set<ClientMetadata> subscribers = this.getSubscription().get(itemKey);
        ClientMetadata clientMetadata = new ClientMetadata(callbackAddress, callbackPort);
        if (!subscribers.contains(clientMetadata)) {
            subscribers.add(clientMetadata);
        }
        this.getConnectedClients().add(clientMetadata);
    }

    public synchronized void unsubscribe(String callbackAddress, int callbackPort, String applicationName, String key) {
        LOGGER.info("[AbstractNode] unsubscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , callbackAddress, callbackPort, applicationName, key);
        ItemKey itemKey = new ItemKey(applicationName, key);
        if (this.getSubscription().get(itemKey) == null) {
            return;
        }
        Set<ClientMetadata> subscribers = this.getSubscription().get(itemKey);
        ClientMetadata clientMetadata = new ClientMetadata(callbackAddress, callbackPort);
        if (subscribers.contains(clientMetadata)) {
            subscribers.remove(clientMetadata);
        }
        boolean exist = false;
        for (Set<ClientMetadata> clients : this.getSubscription().values()) {
            if (clients.contains(clientMetadata)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            this.getConnectedClients().remove(clientMetadata);
        }
    }
}
