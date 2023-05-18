package ripple.common.tcp;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Message {
    private MessageType type;
    private UUID uuid;

    public MessageType getType() {
        return type;
    }

    protected void setType(MessageType type) {
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
