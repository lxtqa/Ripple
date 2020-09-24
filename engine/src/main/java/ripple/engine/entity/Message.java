package ripple.engine.entity;

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

    public void setClusterUuid(UUID clusterUuid) {
        this.clusterUuid = clusterUuid;
    }

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public UUID getNodeUuid() {
        return nodeUuid;
    }

    public void setNodeUuid(UUID nodeUuid) {
        this.nodeUuid = nodeUuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
