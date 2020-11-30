package ripple.client;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.client.core.Endpoint;
import ripple.client.core.Item;
import ripple.client.core.callback.NotifyServlet;
import ripple.client.core.ui.AddSubscriptionServlet;
import ripple.client.core.ui.DeleteConfigServlet;
import ripple.client.core.ui.GetConfigServlet;
import ripple.client.core.ui.GetSubscriptionServlet;
import ripple.client.core.ui.HomeServlet;
import ripple.client.core.ui.ModifyConfigServlet;
import ripple.client.core.ui.NewConfigServlet;
import ripple.client.core.ui.RemoveSubscriptionServlet;
import ripple.client.core.ui.ServerInfoServlet;
import ripple.client.core.ui.StyleServlet;
import ripple.client.helper.Api;
import ripple.client.helper.Storage;

import java.net.InetAddress;

/**
 * @author Zhen Tang
 */
public class RippleClient {
    private String serverAddress;
    private int serverPort;
    private Storage storage;
    private String address;
    private int port;
    private Server server;
    private boolean running;

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

    public RippleClient(String serverAddress, int serverPort, String storageLocation) {
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
        this.setStorage(new Storage(storageLocation));
        this.setRunning(false);
    }

    public Item get(String applicationName, String key) {
        return this.loadItem(applicationName, key);
    }

    public boolean put(String applicationName, String key, String value) {
        boolean result = Api.put(this.getServerAddress(), this.getServerPort(), applicationName, key, value);
        this.loadItem(applicationName, key);
        return result;
    }

    private Item loadItem(String applicationName, String key) {
        Item item = this.getStorage().get(applicationName, key);
        if (item == null) {
            item = Api.get(this.getServerAddress(), this.getServerPort(), applicationName, key);
            this.getStorage().put(item);
        }
        return item;
    }

    public boolean subscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        return Api.subscribe(this.getServerAddress(), this.getServerPort()
                , this.getAddress(), this.getPort(), applicationName, key);
    }

    public boolean unsubscribe(String applicationName, String key) {
        if (!this.isRunning()) {
            this.start();
        }
        return Api.unsubscribe(this.getServerAddress(), this.getServerPort()
                , this.getAddress(), this.getPort(), applicationName, key);
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

        NewConfigServlet newConfigServlet = new NewConfigServlet(this);
        ServletHolder newConfigServletHolder = new ServletHolder(newConfigServlet);
        servletContextHandler.addServlet(newConfigServletHolder, Endpoint.UI_NEW_CONFIG);

        ModifyConfigServlet modifyConfigServlet = new ModifyConfigServlet(this);
        ServletHolder modifyConfigServletHolder = new ServletHolder(modifyConfigServlet);
        servletContextHandler.addServlet(modifyConfigServletHolder, Endpoint.UI_MODIFY_CONFIG);

        DeleteConfigServlet deleteConfigServlet = new DeleteConfigServlet(this);
        ServletHolder deleteConfigServletHolder = new ServletHolder(deleteConfigServlet);
        servletContextHandler.addServlet(deleteConfigServletHolder, Endpoint.UI_DELETE_CONFIG);

        GetSubscriptionServlet getSubscriptionServlet = new GetSubscriptionServlet(this);
        ServletHolder getSubscriptionServletHolder = new ServletHolder(getSubscriptionServlet);
        servletContextHandler.addServlet(getSubscriptionServletHolder, Endpoint.UI_GET_SUBSCRIPTION);

        AddSubscriptionServlet addSubscriptionServlet = new AddSubscriptionServlet(this);
        ServletHolder addSubscriptionServletHolder = new ServletHolder(addSubscriptionServlet);
        servletContextHandler.addServlet(addSubscriptionServletHolder, Endpoint.UI_ADD_SUBSCRIPTION);

        RemoveSubscriptionServlet removeSubscriptionServlet = new RemoveSubscriptionServlet(this);
        ServletHolder removeSubscriptionServletHolder = new ServletHolder(removeSubscriptionServlet);
        servletContextHandler.addServlet(removeSubscriptionServletHolder, Endpoint.UI_REMOVE_SUBSCRIPTION);

        ServerInfoServlet serverInfoServlet = new ServerInfoServlet(this);
        ServletHolder serverInfoServletHolder = new ServletHolder(serverInfoServlet);
        servletContextHandler.addServlet(serverInfoServletHolder, Endpoint.UI_SERVER_INFO);

        // Business
        NotifyServlet notifyServlet = new NotifyServlet(this);
        ServletHolder notifyServletHolder = new ServletHolder(notifyServlet);
        servletContextHandler.addServlet(notifyServletHolder, Endpoint.CLIENT_NOTIFY);
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
}
