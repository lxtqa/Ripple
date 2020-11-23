package ripple.client.core;

import java.util.Date;

public class Item {
    private String key;
    private String value;
    private int lastUpdateServerId;
    private Date lastUpdate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}