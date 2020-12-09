package ripple.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Endpoint;
import ripple.common.Item;
import ripple.common.ItemKey;
import ripple.server.core.api.DeleteServlet;
import ripple.server.core.api.GetServlet;
import ripple.server.core.api.HeartbeatServlet;
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

import javax.servlet.Servlet;
import java.net.InetAddress;
import java.util.ArrayList;
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
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private int id;
    private Overlay overlay;
    private Storage storage;
    private List<NodeMetadata> nodeList;
    private ConcurrentHashMap<ItemKey, Set<ClientMetadata>> subscription;
    private Set<ClientMetadata> connectedClients;

    private String address;
    private int port;
    private Server server;
    private boolean running;

    public Node(int id, Overlay overlay, String storageLocation) {
        this(id, overlay, storageLocation, 0);
    }

    public Node(int id, Overlay overlay, String storageLocation, int port) {
        this.setId(id);
        this.setOverlay(overlay);
        this.setNodeList(new ArrayList<>());
        this.setStorage(new Storage(storageLocation));
        this.setSubscription(new ConcurrentHashMap<>());
        this.setPort(port);
        this.setConnectedClients(new HashSet<>());
    }

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

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
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

    public ObjectMapper getObjectMapper() {
        return MAPPER;
    }

    private void registerServlet(ServletContextHandler servletContextHandler, Servlet servlet, String endpoint) {
        servletContextHandler.addServlet(new ServletHolder(servlet), endpoint);
    }

    public void registerHandlers(ServletContextHandler servletContextHandler) {
        // UI
        this.registerServlet(servletContextHandler, new HomeServlet(this), Endpoint.UI_HOME);
        this.registerServlet(servletContextHandler, new StyleServlet(this), Endpoint.UI_STYLE);
        this.registerServlet(servletContextHandler, new GetConfigServlet(this), Endpoint.UI_GET_CONFIG);
        this.registerServlet(servletContextHandler, new AddConfigServlet(this), Endpoint.UI_ADD_CONFIG);
        this.registerServlet(servletContextHandler, new ModifyConfigServlet(this), Endpoint.UI_MODIFY_CONFIG);
        this.registerServlet(servletContextHandler, new RemoveConfigServlet(this), Endpoint.UI_REMOVE_CONFIG);
        this.registerServlet(servletContextHandler, new GetSubscriptionServlet(this), Endpoint.UI_GET_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new ServerClusterServlet(this), Endpoint.UI_SERVER_CLUSTER);
        this.registerServlet(servletContextHandler, new ClientClusterServlet(this), Endpoint.UI_CLIENT_CLUSTER);

        // API
        this.registerServlet(servletContextHandler, new SubscribeServlet(this), Endpoint.SERVER_SUBSCRIBE);
        this.registerServlet(servletContextHandler, new UnsubscribeServlet(this), Endpoint.SERVER_UNSUBSCRIBE);
        this.registerServlet(servletContextHandler, new GetServlet(this), Endpoint.SERVER_GET);
        this.registerServlet(servletContextHandler, new PutServlet(this), Endpoint.SERVER_PUT);
        this.registerServlet(servletContextHandler, new DeleteServlet(this), Endpoint.SERVER_DELETE);
        this.registerServlet(servletContextHandler, new SyncServlet(this), Endpoint.SERVER_SYNC);
        this.registerServlet(servletContextHandler, new HeartbeatServlet(this), Endpoint.SERVER_HEARTBEAT);
    }

    public Item get(String applicationName, String key) {
        return this.getStorage().get(applicationName, key);
    }

    public List<Item> getAll() {
        return this.getStorage().getAll();
    }

    public boolean put(String applicationName, String key, String value) {
        // Update local storage
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();
        Item item = this.doUpdateItem(applicationName, key, value, lastUpdate, lastUpdateServerId);

        // Notify clients
        this.doNotifyUpdateToClients(item);

        int sourceId = lastUpdateServerId;
        int currentId = this.getId();

        this.doSyncUpdate(applicationName, key, value, lastUpdate, lastUpdateServerId, sourceId, currentId);

        return true;
    }

    public boolean delete(String applicationName, String key) {
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();

        // Update local storage
        this.doDeleteItem(applicationName, key);

        // Notify clients
        this.doNotifyDeleteToClients(applicationName, key);

        int sourceId = lastUpdateServerId;
        int currentId = this.getId();

        this.doSyncDelete(applicationName, key, lastUpdate, lastUpdateServerId, sourceId, currentId);

        return true;
    }

    private void doDeleteItem(String applicationName, String key) {
        Item item = this.getStorage().get(applicationName, key);
        if (item != null) {
            this.getStorage().delete(item);
        }
    }

    private NodeMetadata findServerById(int serverId) {
        for (NodeMetadata nodeMetadata : this.getNodeList()) {
            if (nodeMetadata.getId() == serverId) {
                return nodeMetadata;
            }
        }
        return null;
    }

    public boolean onSyncUpdateReceived(String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId) {
        // Update local storage
        Item item = this.doUpdateItem(applicationName, key, value, lastUpdate, lastUpdateServerId);

        // Notify clients
        this.doNotifyUpdateToClients(item);

        int sourceId = lastUpdateServerId;
        int currentId = this.getId();

        this.doSyncUpdate(applicationName, key, value, lastUpdate, lastUpdateServerId, sourceId, currentId);

        return true;
    }

    private void doSyncUpdate(String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId, int sourceId, int currentId) {
        NodeMetadata source = this.findServerById(sourceId);
        NodeMetadata current = this.findServerById(currentId);
        List<NodeMetadata> toSend = this.getOverlay().calculateNodesToSync(source, current);

        // Sync to servers following the overlay
        for (NodeMetadata metadata : toSend) {
            LOGGER.info("[Node] Sync update to server {}:{}.", metadata.getAddress(), metadata.getPort());
            Api.syncUpdateToServer(metadata, applicationName, key, value, lastUpdate, lastUpdateServerId);
        }
    }

    public boolean onSyncDeleteReceived(String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        // Update local storage
        this.doDeleteItem(applicationName, key);

        // Notify clients
        this.doNotifyDeleteToClients(applicationName, key);

        int sourceId = lastUpdateServerId;
        int currentId = this.getId();

        this.doSyncDelete(applicationName, key, lastUpdate, lastUpdateServerId, sourceId, currentId);

        return true;
    }

    private void doSyncDelete(String applicationName, String key, Date lastUpdate, int lastUpdateServerId, int sourceId, int currentId) {
        NodeMetadata source = this.findServerById(sourceId);
        NodeMetadata current = this.findServerById(currentId);
        List<NodeMetadata> toSend = this.getOverlay().calculateNodesToSync(source, current);

        // Sync to servers following the overlay
        for (NodeMetadata metadata : toSend) {
            LOGGER.info("[Node] Sync delete to server {}:{}.", metadata.getAddress(), metadata.getPort());
            Api.syncDeleteToServer(metadata, applicationName, key, lastUpdate, lastUpdateServerId);
        }
    }

    private void doNotifyUpdateToClients(Item item) {
        ItemKey itemKey = new ItemKey(item.getApplicationName(), item.getKey());
        if (this.getSubscription().containsKey(itemKey)) {
            Set<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[Node] Notify update to client {}:{}.", metadata.getAddress(), metadata.getPort());
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

    private void doNotifyDeleteToClients(String applicationName, String key) {
        ItemKey itemKey = new ItemKey(applicationName, key);
        if (this.getSubscription().containsKey(itemKey)) {
            Set<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[Node] Notify delete to client {}:{}.", metadata.getAddress(), metadata.getPort());
                Api.notifyDeleteToClient(metadata, applicationName, key);
            }
        }
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

    public void initCluster(List<NodeMetadata> nodeList) {
        this.setNodeList(nodeList);
        this.getOverlay().buildOverlay(this.getNodeList());
    }
}
