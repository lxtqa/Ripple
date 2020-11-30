package ripple.server.core.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.core.AbstractNode;
import ripple.server.core.Endpoint;
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

/**
 * @author Zhen Tang
 */
public class StarNode extends AbstractNode {
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

        SyncServlet syncServlet = new SyncServlet(this);
        ServletHolder syncServletHolder = new ServletHolder(syncServlet);
        servletContextHandler.addServlet(syncServletHolder, Endpoint.SERVER_SYNC);
    }
}
