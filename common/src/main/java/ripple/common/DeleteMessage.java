package ripple.common;

import java.util.Date;
import java.util.UUID;

public class DeleteMessage extends Message {
    private String applicationName;
    private String key;
    private int lastUpdateServerId;
    private Date lastUpdate;

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

    public DeleteMessage(UUID uuid, String type) {
        super(uuid, type);
    }

    public DeleteMessage(String type) {
        super(type);
    }
}
