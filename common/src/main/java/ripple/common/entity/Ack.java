package ripple.common.entity;

import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Ack {
    private UUID messageUuid;
    private List<Integer> nodeList;
    private List<Integer> ackNodes;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public List<Integer> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Integer> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Integer> getAckNodes() {
        return ackNodes;
    }

    public void setAckNodes(List<Integer> ackNodes) {
        this.ackNodes = ackNodes;
    }
}
