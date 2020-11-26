package ripple.server;

import ripple.server.core.AbstractNode;
import ripple.server.core.NodeMetadata;
import ripple.server.core.NodeType;
import ripple.server.core.star.StarNode;

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

    public RippleServer(String type, int id, String storageLocation) {
        if (type.equals(NodeType.STAR)) {
            this.setNode(new StarNode(id, storageLocation));
        }
    }

    public RippleServer(String type, int id, String storageLocation, int port) {
        if (type.equals(NodeType.STAR)) {
            this.setNode(new StarNode(id, storageLocation, port));
        }
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
