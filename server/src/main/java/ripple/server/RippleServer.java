package ripple.server;

import ripple.server.core.Node;
import ripple.server.core.NodeMetadata;
import ripple.server.core.overlay.ExpanderOverlay;
import ripple.server.core.overlay.GossipOverlay;
import ripple.server.core.overlay.StarOverlay;
import ripple.server.core.overlay.TreeOverlay;

import java.util.Set;

/**
 * @author Zhen Tang
 */
public class RippleServer {
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    private RippleServer(Node node) {
        this.setNode(node);
    }

    public static RippleServer starProtocol(int id, String storageLocation) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation));
    }

    public static RippleServer starProtocol(int id, String storageLocation, int port) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation, port));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int port, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation, port));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int port, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation, port));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int port, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation, port));
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

    public Set<NodeMetadata> getNodeList() {
        return this.getNode().getNodeList();
    }

    public void setNodeList(Set<NodeMetadata> nodeList) {
        this.getNode().setNodeList(nodeList);
    }
}
