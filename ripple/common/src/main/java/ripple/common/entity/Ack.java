package ripple.common.entity;

import java.util.Set;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Ack {
    private UUID messageUuid;
    private Set<Integer> nodeList;
    private Set<Integer> ackNodes;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public Set<Integer> getNodeList() {
        return nodeList;
    }

    public void setNodeList(Set<Integer> nodeList) {
        this.nodeList = nodeList;
    }

    public Set<Integer> getAckNodes() {
        return ackNodes;
    }

    public void setAckNodes(Set<Integer> ackNodes) {
        this.ackNodes = ackNodes;
    }
}
