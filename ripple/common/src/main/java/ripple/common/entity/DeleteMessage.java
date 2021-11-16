package ripple.common.entity;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class DeleteMessage extends Message {

    public DeleteMessage(UUID uuid, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        super(uuid, Constants.MESSAGE_TYPE_DELETE, applicationName, key, lastUpdate, lastUpdateServerId);
    }

    public DeleteMessage(String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, lastUpdate, lastUpdateServerId);
    }

    public DeleteMessage() {

    }

}
