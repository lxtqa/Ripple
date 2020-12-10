package ripple.common;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Message {
    private UUID uuid;
    private String type;

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

    public Message(UUID uuid, String type) {
        this.setUuid(uuid);
        this.setType(type);
    }

    public Message(String type) {
        this.setUuid(UUID.randomUUID());
        this.setType(type);
    }
}
