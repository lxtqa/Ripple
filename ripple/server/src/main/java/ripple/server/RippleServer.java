package ripple.server;

import ripple.server.core.Node;
import ripple.common.entity.NodeMetadata;
import ripple.server.core.overlay.ExpanderOverlay;
import ripple.server.core.overlay.GossipOverlay;
import ripple.server.core.overlay.StarOverlay;
import ripple.server.core.overlay.tree.TreeOverlay;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class RippleServer {
    private Node node;

    private RippleServer(Node node) {
        this.setNode(node);
    }

    public static RippleServer starProtocol(int id, String storageLocation) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation));
    }

    public static RippleServer starProtocol(int id, String storageLocation, int nettyPort, int uiPort) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation, nettyPort, uiPort));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int nettyPort, int uiPort, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation, nettyPort, uiPort));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int nettyPort, int uiPort, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation, nettyPort, uiPort));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int nettyPort, int uiPort, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation, nettyPort, uiPort));
    }

    public Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
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

    public int getApiPort() {
        return this.getNode().getApiPort();
    }

    public int getUiPort() {
        return this.getNode().getUiPort();
    }

    public void initCluster(List<NodeMetadata> nodeList) {
        this.getNode().initCluster(nodeList);
        this.getNode().getHealthManager().init();
    }
}
