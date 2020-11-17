package ripple.server.protocol.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.AbstractNode;
import ripple.server.NodeType;
import ripple.server.Endpoint;

public class StarNode extends AbstractNode {
    public StarNode(int id, int port) {
        super(id, NodeType.STAR, port);
    }

    public StarNode(int id) {
        super(id, NodeType.STAR);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        SubscribeServlet subscribeServlet = new SubscribeServlet(this);
        servletContextHandler.addServlet(new ServletHolder(subscribeServlet), Endpoint.SUBSCRIBE);
        UnsubscribeServlet unsubscribeServlet = new UnsubscribeServlet(this);
        servletContextHandler.addServlet(new ServletHolder(unsubscribeServlet), Endpoint.UNSUBSCRIBE);
        GetServlet getServlet = new GetServlet(this);
        servletContextHandler.addServlet(new ServletHolder(getServlet), Endpoint.GET);
        PutServlet putServlet = new PutServlet(this);
        servletContextHandler.addServlet(new ServletHolder(putServlet), Endpoint.PUT);
        SyncServlet syncServlet = new SyncServlet(this);
        servletContextHandler.addServlet(new ServletHolder(syncServlet), Endpoint.SYNC);
    }
}
