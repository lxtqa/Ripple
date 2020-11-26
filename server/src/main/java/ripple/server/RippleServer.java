package ripple.server;

import ripple.server.core.AbstractNode;
import ripple.server.core.NodeMetadata;
import ripple.server.core.expander.ExpanderNode;
import ripple.server.core.gossip.GossipNode;
import ripple.server.core.star.StarNode;
import ripple.server.core.tree.TreeNode;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class RippleServer {
    private AbstractNode node;

    private AbstractNode getNode() {
        return node;
    }

    private void setNode(AbstractNode node) {
        this.node = node;
    }

    private RippleServer(AbstractNode node) {
        this.setNode(node);
    }

    public static RippleServer starProtocol(int id, String storageLocation) {
        return new RippleServer(new StarNode(id, storageLocation));
    }

    public static RippleServer starProtocol(int id, String storageLocation, int port) {
        return new RippleServer(new StarNode(id, storageLocation, port));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int branch) {
        return new RippleServer(new TreeNode(id, storageLocation, branch));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int port, int branch) {
        return new RippleServer(new TreeNode(id, storageLocation, port, branch));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int scale) {
        return new RippleServer(new ExpanderNode(id, storageLocation, scale));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int port, int scale) {
        return new RippleServer(new ExpanderNode(id, storageLocation, port, scale));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int fanout) {
        return new RippleServer(new GossipNode(id, storageLocation, fanout));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int port, int fanout) {
        return new RippleServer(new GossipNode(id, storageLocation, port, fanout));
    }

    public boolean start() {
        return this.getNode().start();
    }

    public boolean stop() {
        return this.getNode().stop();
    }

    public boolean isRunning() {
        return this.getNode().isRunning();
    }

    public int getId() {
        return this.getNode().getId();
    }

    public String getAddress() {
        return this.getNode().getAddress();
    }

    public int getPort() {
        return this.getNode().getPort();
    }

    public List<NodeMetadata> getNodeList() {
        return this.getNode().getNodeList();
    }

    public void setNodeList(List<NodeMetadata> nodeList) {
        this.getNode().setNodeList(nodeList);
    }
}
