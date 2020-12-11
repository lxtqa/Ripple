package ripple.common;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Message {
    private UUID uuid;
    private String type;
    private String applicationName;
    private String key;
    private int lastUpdateServerId;
    private Date lastUpdate;

    public UUID getUuid() {
        return uuid;
    }

    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getApplicationName() {
        return applicationName;
    }

    private void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public int getLastUpdateServerId() {
        return lastUpdateServerId;
    }

    private void setLastUpdateServerId(int lastUpdateServerId) {
        this.lastUpdateServerId = lastUpdateServerId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    private void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Message(UUID uuid, String type, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        this.setUuid(uuid);
        this.setType(type);
        this.setApplicationName(applicationName);
        this.setKey(key);
        this.setLastUpdate(lastUpdate);
        this.setLastUpdateServerId(lastUpdateServerId);
    }

    public Message(String type, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), type, applicationName, key, lastUpdate, lastUpdateServerId);
    }
}
