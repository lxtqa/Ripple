package ripple.server.core.overlay.tree;

import ripple.server.core.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TreeNode {
    private NodeMetadata nodeMetadata;
    private List<TreeNode> children;

    public TreeNode(NodeMetadata nodeMetadata) {
        this.setNodeMetadata(nodeMetadata);
        this.setChildren(new ArrayList<>());
    }

    public NodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    private void setNodeMetadata(NodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    private void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
