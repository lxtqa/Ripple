package ripple.server.core;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.api.DeleteServlet;
import ripple.server.core.api.GetServlet;
import ripple.server.core.api.PutServlet;
import ripple.server.core.api.SubscribeServlet;
import ripple.server.core.api.SyncServlet;
import ripple.server.core.api.UnsubscribeServlet;
import ripple.server.core.overlay.Overlay;
import ripple.server.core.ui.AddConfigServlet;
import ripple.server.core.ui.ClientClusterServlet;
import ripple.server.core.ui.GetConfigServlet;
import ripple.server.core.ui.GetSubscriptionServlet;
import ripple.server.core.ui.HomeServlet;
import ripple.server.core.ui.ModifyConfigServlet;
import ripple.server.core.ui.RemoveConfigServlet;
import ripple.server.core.ui.ServerClusterServlet;
import ripple.server.core.ui.StyleServlet;
import ripple.server.helper.Api;
import ripple.server.helper.Storage;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class Node {
    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    private int id;
    private Overlay overlay;
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

    public Overlay getOverlay() {
        return overlay;
    }

    public void setOverlay(Overlay overlay) {
        this.overlay = overlay;
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

    public void registerHandlers(ServletContextHandler servletContextHandler) {
        // UI
        HomeServlet homeServlet = new HomeServlet(this);
        ServletHolder homeServletHolder = new ServletHolder(homeServlet);
        servletContextHandler.addServlet(homeServletHolder, Endpoint.UI_HOME);

        StyleServlet styleServlet = new StyleServlet(this);
        ServletHolder styleServletHolder = new ServletHolder(styleServlet);
        servletContextHandler.addServlet(styleServletHolder, Endpoint.UI_STYLE);

        GetConfigServlet getConfigServlet = new GetConfigServlet(this);
        ServletHolder getConfigServletHolder = new ServletHolder(getConfigServlet);
        servletContextHandler.addServlet(getConfigServletHolder, Endpoint.UI_GET_CONFIG);

        AddConfigServlet addConfigServlet = new AddConfigServlet(this);
        ServletHolder addConfigServletHolder = new ServletHolder(addConfigServlet);
        servletContextHandler.addServlet(addConfigServletHolder, Endpoint.UI_ADD_CONFIG);

        ModifyConfigServlet modifyConfigServlet = new ModifyConfigServlet(this);
        ServletHolder modifyConfigServletHolder = new ServletHolder(modifyConfigServlet);
        servletContextHandler.addServlet(modifyConfigServletHolder, Endpoint.UI_MODIFY_CONFIG);

        RemoveConfigServlet removeConfigServlet = new RemoveConfigServlet(this);
        ServletHolder removeConfigServletHolder = new ServletHolder(removeConfigServlet);
        servletContextHandler.addServlet(removeConfigServletHolder, Endpoint.UI_REMOVE_CONFIG);

        GetSubscriptionServlet getSubscriptionServlet = new GetSubscriptionServlet(this);
        ServletHolder getSubscriptionServletHolder = new ServletHolder(getSubscriptionServlet);
        servletContextHandler.addServlet(getSubscriptionServletHolder, Endpoint.UI_GET_SUBSCRIPTION);

        ServerClusterServlet serverClusterServlet = new ServerClusterServlet(this);
        ServletHolder serverClusterServletHolder = new ServletHolder(serverClusterServlet);
        servletContextHandler.addServlet(serverClusterServletHolder, Endpoint.UI_SERVER_CLUSTER);

        ClientClusterServlet clientClusterServlet = new ClientClusterServlet(this);
        ServletHolder clientClusterServletHolder = new ServletHolder(clientClusterServlet);
        servletContextHandler.addServlet(clientClusterServletHolder, Endpoint.UI_CLIENT_CLUSTER);

        // API
        SubscribeServlet subscribeServlet = new SubscribeServlet(this);
        ServletHolder subscribeServletHolder = new ServletHolder(subscribeServlet);
        servletContextHandler.addServlet(subscribeServletHolder, Endpoint.SERVER_SUBSCRIBE);

        UnsubscribeServlet unsubscribeServlet = new UnsubscribeServlet(this);
        ServletHolder unsubscribeServletHolder = new ServletHolder(unsubscribeServlet);
        servletContextHandler.addServlet(unsubscribeServletHolder, Endpoint.SERVER_UNSUBSCRIBE);

        GetServlet getServlet = new GetServlet(this);
        ServletHolder getServletHolder = new ServletHolder(getServlet);
        servletContextHandler.addServlet(getServletHolder, Endpoint.SERVER_GET);

        PutServlet putServlet = new PutServlet(this);
        ServletHolder putServletHolder = new ServletHolder(putServlet);
        servletContextHandler.addServlet(putServletHolder, Endpoint.SERVER_PUT);

        DeleteServlet deleteServlet = new DeleteServlet(this);
        ServletHolder deleteServletHolder = new ServletHolder(deleteServlet);
        servletContextHandler.addServlet(deleteServletHolder, Endpoint.SERVER_DELETE);

        SyncServlet syncServlet = new SyncServlet(this);
        ServletHolder syncServletHolder = new ServletHolder(syncServlet);
        servletContextHandler.addServlet(syncServletHolder, Endpoint.SERVER_SYNC);
    }

    public Item get(String applicationName, String key) {
        return this.getStorage().get(applicationName, key);
    }

    public List<Item> getAll() {
        return this.getStorage().getAll();
    }

    public boolean put(String applicationName, String key, String value) {
        // Update local storage
        Item item = this.doUpdateItem(applicationName, key, value, new Date(System.currentTimeMillis()), this.getId());

        // Notify clients
        this.doNotifyUpdateToClients(item);

        // [Star protocol] Sync to all the other servers
        for (NodeMetadata metadata : this.getNodeList()) {
            if (metadata.getId() == this.getId()
                    && metadata.getAddress().equals(this.getAddress())
                    && metadata.getPort() == this.getPort()) {
                continue;
            }
            LOGGER.info("[StarNode] Sync update to server {}:{}.", metadata.getAddress(), metadata.getPort());
            Api.syncUpdateToServer(metadata, item);
        }
        return true;
    }

    public boolean delete(String applicationName, String key) {
        // Update local storage
        this.doDeleteItem(applicationName, key);

        // Notify clients
        this.doNotifyDeleteToClients(applicationName, key);

        // [Star protocol] Sync to all the other servers
        for (NodeMetadata metadata : this.getNodeList()) {
            if (metadata.getId() == this.getId()
                    && metadata.getAddress().equals(this.getAddress())
                    && metadata.getPort() == this.getPort()) {
                continue;
            }
            LOGGER.info("[StarNode] Sync delete to server {}:{}.", metadata.getAddress(), metadata.getPort());
            Api.syncDeleteToServer(metadata, applicationName, key);
        }
        return true;
    }

    private void doDeleteItem(String applicationName, String key) {
        Item item = this.getStorage().get(applicationName, key);
        if (item != null) {
            this.getStorage().delete(item);
        }
    }

    public boolean onSyncUpdateReceived(String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId) {
        // Update local storage
        Item item = this.doUpdateItem(applicationName, key, value, lastUpdate, lastUpdateServerId);

        // Notify clients
        this.doNotifyUpdateToClients(item);

        return true;
    }

    private void doNotifyUpdateToClients(Item item) {
        ItemKey itemKey = new ItemKey(item.getApplicationName(), item.getKey());
        if (this.getSubscription().containsKey(itemKey)) {
            Set<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[SyncServlet] Notify update to client {}:{}.", metadata.getAddress(), metadata.getPort());
                Api.notifyUpdateToClient(metadata, item);
            }
        }
    }

    private Item doUpdateItem(String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId) {
        Item item = this.getStorage().get(applicationName, key);
        if (item == null) {
            item = new Item();
        }
        synchronized (this) {
            item.setApplicationName(applicationName);
            item.setKey(key);
            item.setValue(value);
            item.setLastUpdate(lastUpdate);
            item.setLastUpdateServerId(lastUpdateServerId);
        }
        this.getStorage().put(item);
        return item;
    }

    public boolean onSyncDeleteReceived(String applicationName, String key) {
        // Update local storage
        this.doDeleteItem(applicationName, key);

        // Notify clients
        this.doNotifyDeleteToClients(applicationName, key);
        return true;
    }

    private void doNotifyDeleteToClients(String applicationName, String key) {
        ItemKey itemKey = new ItemKey(applicationName, key);
        if (this.getSubscription().containsKey(itemKey)) {
            Set<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[SyncServlet] Notify delete to client {}:{}.", metadata.getAddress(), metadata.getPort());
                Api.notifyDeleteToClient(metadata, applicationName, key);
            }
        }
    }

    public Node(int id, Overlay overlay, String storageLocation) {
        this(id, overlay, storageLocation, 0);
    }

    public Node(int id, Overlay overlay, String storageLocation, int port) {
        this.setId(id);
        this.setOverlay(overlay);
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
        LOGGER.info("[Node] subscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
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
        LOGGER.info("[Node] unsubscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
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

        this.cleanUpConnectedClients(clientMetadata);
    }

    private void cleanUpConnectedClients(ClientMetadata clientMetadata) {
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
