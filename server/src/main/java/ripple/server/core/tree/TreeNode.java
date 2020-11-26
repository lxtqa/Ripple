package ripple.server.core.tree;

import org.eclipse.jetty.servlet.ServletContextHandler;
import ripple.server.core.AbstractNode;
import ripple.server.core.NodeType;

/**
 * @author Zhen Tang
 */
public class TreeNode extends AbstractNode {
    private int branch;

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public TreeNode(int id, String storageLocation, int port, int branch) {
        super(id, NodeType.TREE, storageLocation, port);
        this.setBranch(branch);
    }

    public TreeNode(int id, String storageLocation, int branch) {
        super(id, NodeType.TREE, storageLocation);
        this.setBranch(branch);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        // TODO
    }
}
