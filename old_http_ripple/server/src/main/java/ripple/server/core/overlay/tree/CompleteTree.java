package ripple.server.core.overlay.tree;

import ripple.server.core.NodeMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Zhen Tang
 */
public class CompleteTree {
    private int branch;
    private List<TreeNode> nodeList;
    private TreeNode root;

    public CompleteTree(int branch, List<NodeMetadata> nodeList) {
        this.setBranch(branch);
        this.setNodeList(new ArrayList<>());
        this.buildCompleteTree(nodeList);
    }

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

    private void buildCompleteTree(List<NodeMetadata> nodeList) {
        int i = 0;
        for (i = 0; i < nodeList.size(); i++) {
            this.getNodeList().add(new TreeNode(nodeList.get(i)));
        }

        Queue<TreeNode> nodes = new ArrayBlockingQueue<>(nodeList.size());
        Queue<TreeNode> toAssign = new ArrayBlockingQueue<>(nodeList.size());
        for (i = 0; i < this.getNodeList().size(); i++) {
            TreeNode treeNode = this.getNodeList().get(i);
            nodes.offer(treeNode);
            toAssign.offer(treeNode);
        }

        this.setRoot(toAssign.poll());
        for (i = 0; i < this.getNodeList().size(); i++) {
            TreeNode treeNode = nodes.poll();
            if (treeNode != null) {
                int j = 0;
                for (j = 0; j < this.getBranch(); j++) {
                    TreeNode element = toAssign.poll();
                    if (element != null) {
                        treeNode.getChildren().add(element);
                    }
                }
            }
        }
    }
}
