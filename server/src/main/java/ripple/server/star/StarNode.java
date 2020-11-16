package ripple.server.star;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ripple.server.AbstractNode;
import ripple.server.NodeType;

public class StarNode extends AbstractNode {
    public StarNode(int id, int port) {
        super(id, NodeType.STAR, port);
    }

    public StarNode(int id) {
        super(id, NodeType.STAR);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        UpdateServlet updateServlet = new UpdateServlet(this);
        servletContextHandler.addServlet(new ServletHolder(updateServlet), StarEndpoint.UPDATE);
        SyncServlet syncServlet = new SyncServlet(this);
        servletContextHandler.addServlet(new ServletHolder(syncServlet), StarEndpoint.SYNC);
    }
}
