package ripple.server.tcp;

import ripple.common.tcp.Message;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckRequest extends Message {
    private UUID messageUuid;
    private int sourceId;
    private int nodeId;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

}