package ripple.server.core.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.core.AbstractNode;
import ripple.server.core.Endpoint;
import ripple.server.core.NodeType;
import ripple.server.core.ui.*;

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
        servletContextHandler.addServlet(new ServletHolder(homeServlet), Endpoint.UI_HOME);

        StyleServlet styleServlet = new StyleServlet(this);
        servletContextHandler.addServlet(new ServletHolder(styleServlet), Endpoint.UI_STYLE);

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
