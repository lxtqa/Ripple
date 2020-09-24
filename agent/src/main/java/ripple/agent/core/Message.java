package ripple.agent.core;

import ripple.agent.core.node.AbstractNode;

import java.util.Date;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class Message {
    private UUID clusterUuid;
    private UUID messageUuid;
    private UUID nodeUuid;
    private String content;
    private Date date;

    public UUID getClusterUuid() {
        return clusterUuid;
    }

    private void setClusterUuid(UUID clusterUuid) {
        this.clusterUuid = clusterUuid;
    }

    public UUID getMessageUuid() {
        return messageUuid;
    }

    private void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public UUID getNodeUuid() {
        return nodeUuid;
    }

    private void setNodeUuid(UUID nodeUuid) {
        this.nodeUuid = nodeUuid;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public Message(AbstractNode node, UUID messageUuid, String content) {
        this.setDate(new Date(System.currentTimeMillis()));
        this.setContent(content);
        this.setNodeUuid(node.getMetadata().getUuid());
        this.setMessageUuid(messageUuid);
        this.setClusterUuid(node.getCluster().getClusterUuid());
    }
}
