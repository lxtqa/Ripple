package ripple.server.core.gossip;

import org.eclipse.jetty.servlet.ServletContextHandler;
import ripple.server.core.AbstractNode;
import ripple.server.core.Item;
import ripple.server.core.NodeType;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class GossipNode extends AbstractNode {
    private int fanout;

    public int getFanout() {
        return fanout;
    }

    public void setFanout(int fanout) {
        this.fanout = fanout;
    }

    public GossipNode(int id, String storageLocation, int port, int fanout) {
        super(id, NodeType.GOSSIP, storageLocation, port);
        this.setFanout(fanout);
    }

    public GossipNode(int id, String storageLocation, int fanout) {
        super(id, NodeType.GOSSIP, storageLocation);
        this.setFanout(fanout);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        // TODO
    }

    @Override
    public Item get(String applicationName, String key) {
        // TODO
        return null;
    }

    @Override
    public List<Item> getAll() {
        // TODO
        return null;
    }

    @Override
    public boolean put(String applicationName, String key, String value) {
        // TODO
        return false;
    }

    @Override
    public boolean delete(String applicationName, String key) {
        // TODO
        return false;
    }
}
