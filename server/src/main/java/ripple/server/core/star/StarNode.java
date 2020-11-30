package ripple.server.core.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.core.AbstractNode;
import ripple.server.core.Endpoint;
import ripple.server.core.NodeType;
import ripple.server.core.ui.GetConfigServlet;
import ripple.server.core.ui.HomeServlet;
import ripple.server.core.ui.NewConfigServlet;
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
        servletContextHandler.addServlet(new ServletHolder(homeServlet), Endpoint.UI_HOME);

        StyleServlet styleServlet = new StyleServlet(this);
        servletContextHandler.addServlet(new ServletHolder(styleServlet), Endpoint.UI_STYLE);

        GetConfigServlet getConfigServlet = new GetConfigServlet(this);
        ServletHolder getConfigServletHolder = new ServletHolder(getConfigServlet);
        servletContextHandler.addServlet(getConfigServletHolder, Endpoint.UI_GET_CONFIG);
        servletContextHandler.addServlet(getConfigServletHolder, Endpoint.UI_GET_CONFIG + "/");

        NewConfigServlet newConfigServlet = new NewConfigServlet(this);
        ServletHolder newConfigServletHolder = new ServletHolder(newConfigServlet);
        servletContextHandler.addServlet(newConfigServletHolder, Endpoint.UI_New_CONFIG);
        servletContextHandler.addServlet(newConfigServletHolder, Endpoint.UI_New_CONFIG + "/");

        // Business
        SubscribeServlet subscribeServlet = new SubscribeServlet(this);
        servletContextHandler.addServlet(new ServletHolder(subscribeServlet), Endpoint.SERVER_SUBSCRIBE);
        UnsubscribeServlet unsubscribeServlet = new UnsubscribeServlet(this);
        servletContextHandler.addServlet(new ServletHolder(unsubscribeServlet), Endpoint.SERVER_UNSUBSCRIBE);
        GetServlet getServlet = new GetServlet(this);
        servletContextHandler.addServlet(new ServletHolder(getServlet), Endpoint.SERVER_GET);
        PutServlet putServlet = new PutServlet(this);
        servletContextHandler.addServlet(new ServletHolder(putServlet), Endpoint.SERVER_PUT);
        SyncServlet syncServlet = new SyncServlet(this);
        servletContextHandler.addServlet(new ServletHolder(syncServlet), Endpoint.SERVER_SYNC);
    }
}
