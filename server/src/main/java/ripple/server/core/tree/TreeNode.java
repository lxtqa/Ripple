package ripple.server.core.tree;

import org.eclipse.jetty.servlet.ServletContextHandler;
import ripple.server.core.AbstractNode;
import ripple.server.core.Item;
import ripple.server.core.NodeType;

import java.util.List;

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
