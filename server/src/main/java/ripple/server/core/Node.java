package ripple.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.DeleteMessage;
import ripple.common.Endpoint;
import ripple.common.Item;
import ripple.common.ItemKey;
import ripple.common.Message;
import ripple.common.UpdateMessage;
import ripple.common.helper.Storage;
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
        this.registerServlet(servletContextHandler, new SubscribeServlet(this), Endpoint.API_SUBSCRIBE);
        this.registerServlet(servletContextHandler, new UnsubscribeServlet(this), Endpoint.API_UNSUBSCRIBE);
        this.registerServlet(servletContextHandler, new GetServlet(this), Endpoint.API_GET);
        this.registerServlet(servletContextHandler, new PutServlet(this), Endpoint.API_PUT);
        this.registerServlet(servletContextHandler, new DeleteServlet(this), Endpoint.API_DELETE);
        this.registerServlet(servletContextHandler, new SyncServlet(this), Endpoint.API_SYNC);
        this.registerServlet(servletContextHandler, new HeartbeatServlet(this), Endpoint.API_HEARTBEAT);
    }

    public Item get(String applicationName, String key) {
        return this.getStorage().get(applicationName, key);
    }

    public List<Item> getAll() {
        return this.getStorage().getAll();
    }

    public boolean put(String applicationName, String key, String value) {
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();

        UpdateMessage updateMessage = new UpdateMessage(applicationName, key, value, lastUpdate, lastUpdateServerId);
        this.handleMessage(updateMessage);

        return true;
    }

    public boolean delete(String applicationName, String key) {
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();

        DeleteMessage deleteMessage = new DeleteMessage(applicationName, key, lastUpdate, lastUpdateServerId);
        this.handleMessage(deleteMessage);

        return true;
    }

    public boolean handleMessage(Message message){
        // Update local storage
        this.applyMessageToStorage(message);

        // Notify clients
        this.doNotifyClients(message);

        int sourceId = message.getLastUpdateServerId();
        int currentId = this.getId();

        this.doSyncWithServer(message, sourceId, currentId);

        return true;
    }

    private NodeMetadata findServerById(int serverId) {
        for (NodeMetadata nodeMetadata : this.getNodeList()) {
            if (nodeMetadata.getId() == serverId) {
                return nodeMetadata;
            }
        }
        return null;
    }

    private void doSyncWithServer(Message message, int sourceId, int currentId) {
        NodeMetadata source = this.findServerById(sourceId);
        NodeMetadata current = this.findServerById(currentId);
        List<NodeMetadata> toSend = this.getOverlay().calculateNodesToSync(source, current);

        // Sync to servers following the overlay
        for (NodeMetadata metadata : toSend) {
            LOGGER.info("[Node] Sync {} with server {}:{}.", message.getType(), metadata.getAddress(), metadata.getPort());
            Api.syncWithServer(metadata, message);
        }
    }

    private void doNotifyClients(Message message) {
        ItemKey itemKey = new ItemKey(message.getApplicationName(), message.getKey());
        if (this.getSubscription().containsKey(itemKey)) {
            Set<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[Node] Notify {} to client {}:{}.", message.getType(), metadata.getAddress(), metadata.getPort());
                Api.notifyClient(metadata, message);
            }
        }
    }

    private void applyMessageToStorage(Message message) {
        Item item = this.getStorage().get(message.getApplicationName(), message.getKey());
        boolean newItem = false;
        if (item == null) {
            item = new Item();
            newItem = true;
        }
        synchronized (this) {
            if (newItem) {
                item.setApplicationName(message.getApplicationName());
                item.setKey(message.getKey());
            }
            boolean exist = false;
            for (Message elem : item.getMessages()) {
                if (elem.getUuid().equals(message.getUuid())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                item.getMessages().add(message);
            }
        }
        this.getStorage().put(item);
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
