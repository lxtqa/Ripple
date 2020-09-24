package ripple.agent.core.node.star;

import ripple.agent.core.Cluster;
import ripple.agent.core.node.AbstractNode;
import ripple.agent.core.node.NodeType;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class StarNode extends AbstractNode {
    public StarNode(UUID clusterUuid, Cluster cluster) {
        super(clusterUuid, NodeType.STAR, cluster);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        UpdateServlet updateServlet = new UpdateServlet(this, this.getCluster());
        servletContextHandler.addServlet(new ServletHolder(updateServlet), StarEndpoint.UPDATE);
        SyncServlet syncServlet = new SyncServlet(this, this.getCluster());
        servletContextHandler.addServlet(new ServletHolder(syncServlet), StarEndpoint.SYNC);
    }
}
