package ripple.server.core.expander;

import org.eclipse.jetty.servlet.ServletContextHandler;
import ripple.server.core.AbstractNode;
import ripple.server.core.NodeType;

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
}
