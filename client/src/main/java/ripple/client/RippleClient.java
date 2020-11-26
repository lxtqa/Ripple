package ripple.client;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.client.core.Endpoint;
import ripple.client.core.Item;
import ripple.client.core.callback.NotifyServlet;
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

    public Item get(String key) {
        return this.loadItem(key);
    }

    public boolean put(String key, String value) {
        boolean result = Api.put(this.getServerAddress(), this.getServerPort(), key, value);
        this.loadItem(key);
        return result;
    }

    private Item loadItem(String key) {
        Item item = this.getStorage().get(key);
        if (item == null) {
            item = Api.get(this.getServerAddress(), this.getServerPort(), key);
            this.getStorage().put(item);
        }
        return item;
    }

    public boolean subscribe(String key) {
        if (!this.isRunning()) {
            this.start();
        }
        return Api.subscribe(this.getServerAddress(), this.getServerPort()
                , this.getAddress(), this.getPort(), key);
    }

    public boolean unsubscribe(String key) {
        if (!this.isRunning()) {
            this.start();
        }
        return Api.unsubscribe(this.getServerAddress(), this.getServerPort()
                , this.getAddress(), this.getPort(), key);
    }

    public void registerHandlers(ServletContextHandler servletContextHandler) {
        NotifyServlet notifyServlet = new NotifyServlet(this);
        servletContextHandler.addServlet(new ServletHolder(notifyServlet), Endpoint.CLIENT_NOTIFY);
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
