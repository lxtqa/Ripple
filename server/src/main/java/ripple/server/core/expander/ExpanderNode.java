package ripple.server.core.expander;

import org.eclipse.jetty.servlet.ServletContextHandler;
import ripple.server.core.AbstractNode;
import ripple.server.core.Item;
import ripple.server.core.NodeType;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class ExpanderNode extends AbstractNode {
    private int scale;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public ExpanderNode(int id, String storageLocation, int port, int scale) {
        super(id, NodeType.EXPANDER, storageLocation, port);
        this.setScale(scale);
    }

    public ExpanderNode(int id, String storageLocation, int scale) {
        super(id, NodeType.EXPANDER, storageLocation);
        this.setScale(scale);
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
