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
import ripple.client.core.tcp.ClientChannelInitializer;
import ripple.client.core.ui.AddConfigServlet;
import ripple.client.core.ui.AddSubscriptionServlet;
import ripple.client.core.ui.GetConfigServlet;
import ripple.client.core.ui.GetSubscriptionServlet;
import ripple.client.core.ui.HomeServlet;
import ripple.client.core.ui.ModifyConfigServlet;
import ripple.client.core.ui.RemoveConfigServlet;
import ripple.client.core.ui.RemoveSubscriptionServlet;
import ripple.client.core.ui.ServerInfoServlet;
import ripple.client.core.ui.StyleServlet;
import ripple.client.helper.Api;
import ripple.common.Endpoint;
import ripple.common.entity.Item;
import ripple.common.storage.Storage;

import javax.servlet.Servlet;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Zhen Tang
 */
public class RippleClient {
    private String serverAddress;
    private int serverPort;
    private Storage storage;
    private String uiAddress;
    private int uiPort;
    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;
    private Server server;
    private boolean running;
    private Set<Item> subscription;

    public RippleClient(String serverAddress, int serverPort, String storageLocation) {
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
        this.setStorage(new Storage(storageLocation));
        this.setRunning(false);
        this.setSubscription(new HashSet<>());
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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Set<Item> getSubscription() {
        return subscription;
    }

    public void setSubscription(Set<Item> subscription) {
        this.subscription = subscription;
    }

    public Item get(String applicationName, String key) {
        return this.refreshItem(applicationName, key);
    }

    private Item refreshItem(String applicationName, String key) {
        Item item = this.getStorage().getItemService().getItem(applicationName, key);
        if (item == null) {
            Api.getAsync(this.getChannel(), applicationName, key);
        }
        return item;
    }

    public void put(String applicationName, String key, String value) {
        Api.putAsync(this.getChannel(), applicationName, key, value);
        this.refreshItem(applicationName, key);
    }

    public void delete(String applicationName, String key) {
        Api.deleteAsync(this.getChannel(), applicationName, key);
        this.refreshItem(applicationName, key);
    }

    public void subscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        this.getSubscription().add(new Item(applicationName, key));
        Api.subscribeAsync(this.getChannel(), applicationName, key);
        this.refreshItem(applicationName, key);
    }

    public boolean unsubscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        this.getSubscription().remove(new Item(applicationName, key));
        return Api.unsubscribe(this.getServerAddress(), this.getServerPort()
                , this.getUiAddress(), this.getUiPort(), applicationName, key);
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
        this.registerServlet(servletContextHandler, new AddSubscriptionServlet(this), Endpoint.UI_ADD_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new RemoveSubscriptionServlet(this), Endpoint.UI_REMOVE_SUBSCRIPTION);
        this.registerServlet(servletContextHandler, new ServerInfoServlet(this), Endpoint.UI_SERVER_INFO);
    }

    public Channel connectToServer(String address, int port) {
        try {
            this.setEventLoopGroup(new NioEventLoopGroup());
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.getEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ClientChannelInitializer(this));

            ChannelFuture future = bootstrap.connect(address, port).sync();
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
            this.connectToServer(this.getServerAddress(), this.getServerPort());
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
            this.getEventLoopGroup().shutdownGracefully();
            this.setRunning(false);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
