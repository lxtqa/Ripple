package ripple.common;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class UpdateMessage extends Message {
    private String value;

    public UpdateMessage(UUID uuid, String applicationName, String key, String value
            , Date lastUpdate, int lastUpdateServerId) {
        super(uuid, MessageType.UPDATE, applicationName, key, lastUpdate, lastUpdateServerId);
        this.setValue(value);
    }

    public UpdateMessage(String applicationName, String key, String value
            , Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, value, lastUpdate, lastUpdateServerId);
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }
}
