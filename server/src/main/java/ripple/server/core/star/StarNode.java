package ripple.server.core.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.AbstractNode;
import ripple.server.Endpoint;
import ripple.server.NodeType;
import ripple.server.core.ui.HomeServlet;
import ripple.server.core.ui.StyleServlet;

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
