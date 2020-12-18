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
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.Item;
import ripple.common.entity.Message;
import ripple.common.entity.UpdateMessage;
import ripple.common.storage.Storage;
import ripple.server.api.AckServlet;
import ripple.server.api.DeleteServlet;
import ripple.server.api.GetServlet;
import ripple.server.api.HeartbeatServlet;
import ripple.server.api.PutServlet;
import ripple.server.api.SubscribeServlet;
import ripple.server.api.SyncServlet;
import ripple.server.api.UnsubscribeServlet;
import ripple.server.core.overlay.Overlay;
import ripple.server.helper.Api;
import ripple.server.ui.AddConfigServlet;
import ripple.server.ui.ClientClusterServlet;
import ripple.server.ui.GetConfigServlet;
import ripple.server.ui.GetSubscriptionServlet;
import ripple.server.ui.HomeServlet;
import ripple.server.ui.ModifyConfigServlet;
import ripple.server.ui.RemoveConfigServlet;
import ripple.server.ui.ServerClusterServlet;
import ripple.server.ui.StyleServlet;

import javax.servlet.Servlet;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

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
    private ConcurrentHashMap<Item, Set<ClientMetadata>> subscription;
    private Set<ClientMetadata> connectedClients;
    private Tracker tracker;
    private HealthManager healthManager;
    private Worker worker;
    private Thread workingThread;

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
        this.setTracker(new Tracker(this));
        this.setStorage(new Storage(storageLocation));
        this.setHealthManager(new HealthManager(this));
        this.setWorker(new Worker(this));

        this.setNodeList(new ArrayList<>());
        this.setSubscription(new ConcurrentHashMap<>());
        this.setPort(port);
        this.setConnectedClients(new HashSet<>());
    }

    public Tracker getTracker() {
        return tracker;
    }

    private void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public HealthManager getHealthManager() {
        return healthManager;
    }

    private void setHealthManager(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    public Worker getWorker() {
        return worker;
    }

    private void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Thread getWorkingThread() {
        return workingThread;
    }

    private void setWorkingThread(Thread workingThread) {
        this.workingThread = workingThread;
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

    public Storage getStorage() {
        return storage;
    }

    private void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }

    public ConcurrentHashMap<Item, Set<ClientMetadata>> getSubscription() {
        return subscription;
    }

    public void setSubscription(ConcurrentHashMap<Item, Set<ClientMetadata>> subscription) {
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
        this.registerServlet(servletContextHandler, new AckServlet(this), Endpoint.API_ACK);
        this.registerServlet(servletContextHandler, new HeartbeatServlet(this), Endpoint.API_HEARTBEAT);
    }

    public Item get(String applicationName, String key) {
        return this.getStorage().getItemService().getItem(applicationName, key);
    }

    public List<Item> getAll() {
        return this.getStorage().getItemService().getAllItems();
    }

    public boolean put(String applicationName, String key, String value) {
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();

        UpdateMessage message = new UpdateMessage(applicationName, key, value, lastUpdate, lastUpdateServerId);
        this.propagateMessage(message);

        return true;
    }

    public boolean delete(String applicationName, String key) {
        Date lastUpdate = new Date(System.currentTimeMillis());
        int lastUpdateServerId = this.getId();

        DeleteMessage message = new DeleteMessage(applicationName, key, lastUpdate, lastUpdateServerId);
        this.propagateMessage(message);

        return true;
    }

    public boolean propagateMessage(Message message) {
        // Update local storage
        this.applyMessageToStorage(message);

        // Init ACK if it is the message source
        if (message.getLastUpdateServerId() == this.getId()) {
            this.getTracker().initProgress(message);
        }

        // Notify clients
        this.doNotifyClients(message);

        int sourceId = message.getLastUpdateServerId();
        int currentId = this.getId();

        this.doSyncWithServer(message, sourceId, currentId);

        return true;
    }

    public NodeMetadata findServerById(int serverId) {
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
        List<NodeMetadata> initialList = this.getOverlay().calculateNodesToSync(source, current);
        Queue<NodeMetadata> sendQueue = new LinkedBlockingDeque<>(initialList);

        // Sync to servers following the overlay
        while (!sendQueue.isEmpty()) {
            // Fault tolerant
            NodeMetadata nodeMetadata = sendQueue.poll();
            if (this.getHealthManager().isAlive(nodeMetadata)) {
                LOGGER.info("[Node-{}] Sync {} with server {}:{}.", this.getId(), message.getType(), nodeMetadata.getAddress(), nodeMetadata.getPort());
                boolean success = Api.sync(nodeMetadata.getAddress(), nodeMetadata.getPort(), message);
                if (success) {
                    LOGGER.info("[Node-{}] Record ACK of message {} from server {}.", this.getId(), message.getUuid(), nodeMetadata.getId());
                    this.getTracker().recordAck(message.getUuid(), message.getLastUpdateServerId(), nodeMetadata.getId());
                }
            } else {
                LOGGER.info("[Node-{}] Server {}:{} (id = {}) is unreachable, attempting to send to its children."
                        , this.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort(), nodeMetadata.getId());
                List<NodeMetadata> list = this.getOverlay().calculateNodesToSync(source, nodeMetadata);
                sendQueue.addAll(list);
            }
        }
    }

    private void doNotifyClients(Message message) {
        Item item = new Item(message.getApplicationName(), message.getKey());
        if (this.getSubscription().containsKey(item)) {
            Set<ClientMetadata> clients = this.getSubscription().get(item);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[Node-{}] Notify {} to client {}:{}."
                        , this.getId(), message.getType(), metadata.getAddress(), metadata.getPort());
                Api.sync(metadata.getAddress(), metadata.getPort(), message);
            }
        }
    }

    private void applyMessageToStorage(Message message) {
        String applicationName = message.getApplicationName();
        String key = message.getKey();
        Item item = this.getStorage().getItemService().getItem(applicationName, key);
        if (item == null) {
            this.getStorage().getItemService().newItem(applicationName, key);
        }
        this.getStorage().getMessageService().newMessage(message);
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
            this.getWorkingThread().interrupt();
            this.setRunning(false);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized void subscribe(String callbackAddress, int callbackPort, String applicationName, String key) {
        LOGGER.info("[Node-{}] subscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , this.getId(), callbackAddress, callbackPort, applicationName, key);
        Item item = new Item(applicationName, key);
        if (this.getSubscription().get(item) == null) {
            this.getSubscription().put(item, new HashSet<>());
        }
        Set<ClientMetadata> subscribers = this.getSubscription().get(item);
        ClientMetadata clientMetadata = new ClientMetadata(callbackAddress, callbackPort);
        if (!subscribers.contains(clientMetadata)) {
            subscribers.add(clientMetadata);
        }
        this.getConnectedClients().add(clientMetadata);
    }

    public synchronized void unsubscribe(String callbackAddress, int callbackPort, String applicationName, String key) {
        LOGGER.info("[Node-{}] unsubscribe() called: Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , this.getId(), callbackAddress, callbackPort, applicationName, key);
        Item item = new Item(applicationName, key);
        if (this.getSubscription().get(item) == null) {
            return;
        }
        Set<ClientMetadata> subscribers = this.getSubscription().get(item);
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
        this.setWorkingThread(new Thread(this.getWorker()));

        this.getWorkingThread().start();
    }
}
