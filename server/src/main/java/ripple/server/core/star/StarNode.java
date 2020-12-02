package ripple.server.core.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.AbstractNode;
import ripple.server.core.ClientMetadata;
import ripple.server.core.Endpoint;
import ripple.server.core.Item;
import ripple.server.core.ItemKey;
import ripple.server.core.NodeMetadata;
import ripple.server.core.NodeType;
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

import java.util.Date;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class StarNode extends AbstractNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(StarNode.class);

    public StarNode(int id, String storageLocation, int port) {
        super(id, NodeType.STAR, storageLocation, port);
    }

    public StarNode(int id, String storageLocation) {
        super(id, NodeType.STAR, storageLocation);
    }

    @Override
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

        // Business
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

    @Override
    public Item get(String applicationName, String key) {
        return this.getStorage().get(applicationName, key);
    }

    @Override
    public List<Item> getAll() {
        return this.getStorage().getAll();
    }

    @Override
    public boolean put(String applicationName, String key, String value) {
        // Update local storage
        Item item = this.getStorage().get(applicationName, key);
        if (item == null) {
            item = new Item();
        }
        synchronized (this) {
            item.setApplicationName(applicationName);
            item.setKey(key);
            item.setValue(value);
            item.setLastUpdate(new Date(System.currentTimeMillis()));
            item.setLastUpdateServerId(this.getId());
        }
        this.getStorage().put(item);

        ItemKey itemKey = new ItemKey(applicationName, key);
        // Notify clients
        if (this.getSubscription().containsKey(itemKey)) {
            List<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[StarNode] Notify client {}:{}.", metadata.getAddress(), metadata.getPort());
                Api.notifyClient(metadata, item);
            }
        }

        // [Star protocol] Sync to all the other servers
        for (NodeMetadata metadata : this.getNodeList()) {
            if (metadata.getId() == this.getId()
                    && metadata.getAddress().equals(this.getAddress())
                    && metadata.getPort() == this.getPort()) {
                continue;
            }
            LOGGER.info("[StarNode] Sync to server {}:{}.", metadata.getAddress(), metadata.getPort());
            Api.syncToServer(metadata, item);
        }
        return true;
    }

    public boolean syncUpdate(String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId) {
        // Update local storage
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

        ItemKey itemKey = new ItemKey(applicationName, key);
        // Notify clients
        if (this.getSubscription().containsKey(itemKey)) {
            List<ClientMetadata> clients = this.getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[SyncServlet] Notify client {}:{}.", metadata.getAddress(), metadata.getPort());
                Api.notifyClient(metadata, item);
            }
        }
        return true;
    }
}
