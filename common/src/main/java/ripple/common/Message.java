package ripple.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Message {
    private UUID uuid;
    private String type;
    private String applicationName;
    private String key;
    private int lastUpdateServerId;
    private Date lastUpdate;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLastUpdateServerId() {
        return lastUpdateServerId;
    }

    public void setLastUpdateServerId(int lastUpdateServerId) {
        this.lastUpdateServerId = lastUpdateServerId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Message() {

    }

    public Message(UUID uuid, String type, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        this.setUuid(uuid);
        this.setType(type);
        this.setApplicationName(applicationName);
        this.setKey(key);
        this.setLastUpdate(lastUpdate);
        this.setLastUpdateServerId(lastUpdateServerId);
    }
}
