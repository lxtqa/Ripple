package ripple.agent.core;

import ripple.agent.core.node.AbstractNode;

import javax.servlet.http.HttpServlet;

/**
 * @author fuxiao.tz
 */
public class BaseServlet extends HttpServlet {
    private AbstractNode node;
    private Cluster cluster;

    public BaseServlet(AbstractNode node, Cluster cluster) {
        this.setNode(node);
        this.setCluster(cluster);
    }

    public AbstractNode getNode() {
        return node;
    }

    private void setNode(AbstractNode node) {
        this.node = node;
    }

    public Cluster getCluster() {
        return cluster;
    }

    private void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}
