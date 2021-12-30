package ripple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.client.core.HashingBasedSelector;
import ripple.client.core.NodeSelector;
import ripple.client.core.tcp.ClientChannelInitializer;
import ripple.client.core.ui.AddConfigServlet;
import ripple.client.core.ui.AddSubscriptionServlet;
import ripple.client.core.ui.Endpoint;
import ripple.client.core.ui.GetConfigServlet;
import ripple.client.core.ui.GetSubscriptionServlet;
import ripple.client.core.ui.HomeServlet;
import ripple.client.core.ui.IncrementalUpdateServlet;
import ripple.client.core.ui.ModifyConfigServlet;
import ripple.client.core.ui.RemoveConfigServlet;
import ripple.client.core.ui.RemoveSubscriptionServlet;
import ripple.client.core.ui.ServerInfoServlet;
import ripple.client.core.ui.StyleServlet;
import ripple.client.helper.Api;
import ripple.common.entity.Item;
import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.ModHashing;
import ripple.common.storage.Storage;

import javax.servlet.Servlet;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author Zhen Tang
 */
public class RippleClient {
    private Storage storage;
    private String uiAddress;
    private int uiPort;
    private NioEventLoopGroup eventLoopGroup;
    private Server server;
    private boolean running;
    private List<NodeMetadata> nodeList;
    private Map<Item, NodeMetadata> mappingCache;
    private Map<Item, NodeMetadata> subscriptions;
    private Map<NodeMetadata, Channel> connections;
    private NodeSelector nodeSelector;

    public RippleClient(List<NodeMetadata> nodeList, NodeSelector nodeSelector, String storageLocation) {
        this.setStorage(new Storage(storageLocation));
        this.setRunning(false);
        this.setNodeList(nodeList);
        this.setMappingCache(new HashMap<>());
        this.setSubscriptions(new HashMap<>());
        this.setConnections(new HashMap<>());
        this.setNodeSelector(nodeSelector);
    }

    public RippleClient(List<NodeMetadata> nodeList, String storageLocation) {
        this(nodeList, new HashingBasedSelector(new ModHashing()), storageLocation);
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getUiAddress() {
        return uiAddress;
    }

    private void setUiAddress(String uiAddress) {
        this.uiAddress = uiAddress;
    }

    public int getUiPort() {
        return uiPort;
    }

    private void setUiPort(int uiPort) {
        this.uiPort = uiPort;
    }

    private NioEventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    private void setEventLoopGroup(NioEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<NodeMetadata> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.nodeList = nodeList;
    }


    public Map<Item, NodeMetadata> getMappingCache() {
        return mappingCache;
    }

    public void setMappingCache(Map<Item, NodeMetadata> mappingCache) {
        this.mappingCache = mappingCache;
    }

    public Map<Item, NodeMetadata> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<Item, NodeMetadata> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Map<NodeMetadata, Channel> getConnections() {
        return connections;
    }

    public void setConnections(Map<NodeMetadata, Channel> connections) {
        this.connections = connections;
    }

    public NodeSelector getNodeSelector() {
        return nodeSelector;
    }

    public void setNodeSelector(NodeSelector nodeSelector) {
        this.nodeSelector = nodeSelector;
    }

    public Item get(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        return this.refreshItem(applicationName, key);
    }

    public void fullyGet(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Api.getAsync(channel, applicationName, key);
    }

    private Item refreshItem(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Item item = this.getStorage().getItemService().getItem(applicationName, key);
        if (item == null) {
            Api.getAsync(channel, applicationName, key);
        }
        return item;
    }

    public void put(String applicationName, String key, String value) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Api.putAsync(channel, applicationName, key, value);
        this.refreshItem(applicationName, key);
    }

    public void delete(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Api.deleteAsync(channel, applicationName, key);
        this.refreshItem(applicationName, key);
    }

    public void incrementalUpdate(String applicationName, String key, UUID baseMessageUuid
            , String atomicOperation, String value) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Api.incrementalUpdateAsync(channel, applicationName, key, baseMessageUuid, atomicOperation, value);
        this.refreshItem(applicationName, key);
    }

    public void subscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        Item item = new Item(applicationName, key);
        this.getSubscriptions().put(item, this.getMappingCache().get(item));
        Api.subscribeAsync(channel, applicationName, key);
    }

    public void unsubscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        Channel channel = this.findOrConnect(applicationName, key);
        this.getSubscriptions().remove(new Item(applicationName, key));
        Api.unsubscribeAsync(channel, applicationName, key);
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
        this.registerServlet(servletContextHandler, new IncrementalUpdateServlet(this), Endpoint.UI_INCREMENTAL_UPDATE);
        this.registerServlet(servletContextHandler, new RemoveConfigServlet(this), Endpoint.UI_REMOVE_CONFIG);
        this.registerServlet(servletContextHandler, new GetSubscriptionServlet(this), Endpoint.UI_GET_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new AddSubscriptionServlet(this), Endpoint.UI_ADD_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new RemoveSubscriptionServlet(this), Endpoint.UI_REMOVE_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new ServerInfoServlet(this), Endpoint.UI_SERVER_INFO);
    }

    private Channel findOrConnect(String applicationName, String key) {
        Item item = new Item(applicationName, key);
        if (this.getMappingCache().get(item) == null) {
            synchronized (this) {
                if (this.getMappingCache().get(item) == null) {
                    this.getMappingCache().put(item
                            , this.getNodeSelector().selectNodeToConnect(applicationName, key, this.getNodeList()));
                }
            }
        }
        NodeMetadata nodeMetadata = this.getMappingCache().get(item);
        Channel channel = this.getConnections().get(nodeMetadata);
        if (channel == null) {
            this.getConnections().put(nodeMetadata, this.connectToServer(nodeMetadata));
        }
        return this.getConnections().get(nodeMetadata);
    }

    public Channel connectToServer(NodeMetadata server) {
        try {
            this.setEventLoopGroup(new NioEventLoopGroup());
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.getEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .option(ChannelOption.SO_REUSEADDR, true) // TODO: Trick
                    .handler(new ClientChannelInitializer(this));

            ChannelFuture future = bootstrap.connect(server.getAddress(), server.getPort()).sync();
            return future.channel();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public synchronized boolean start() {
        if (this.isRunning()) {
            return true;
        }
        try {
            this.setServer(new Server());
            ServerConnector serverConnector = new ServerConnector(this.getServer());
            serverConnector.setPort(0);
            this.getServer().setConnectors(new Connector[]{serverConnector});

            ServletContextHandler servletContextHandler = new ServletContextHandler();

            this.registerHandlers(servletContextHandler);

            this.getServer().setHandler(servletContextHandler);
            this.getServer().start();
            this.setUiAddress(InetAddress.getLocalHost().getHostAddress());
            this.setUiPort(serverConnector.getLocalPort());
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
            CountDownLatch lock = new CountDownLatch(1);
            this.getEventLoopGroup().shutdownGracefully().addListener(e -> {
                lock.countDown();
            });
            lock.await();
            this.setRunning(false);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
