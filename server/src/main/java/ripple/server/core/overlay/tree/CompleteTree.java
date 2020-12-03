package ripple.server.core.overlay.tree;

import ripple.server.core.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class CompleteTree {
    private int branch;
    private List<TreeNode> nodeList;
    private TreeNode root;

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public List<TreeNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TreeNode> nodeList) {
        this.nodeList = nodeList;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public CompleteTree(int branch, List<NodeMetadata> nodeList) {
        this.setBranch(branch);
        this.setNodeList(new ArrayList<>());
        this.buildCompleteTree(nodeList);
    }

    private void buildCompleteTree(List<NodeMetadata> nodeList) {
        // TODO

    }
}
