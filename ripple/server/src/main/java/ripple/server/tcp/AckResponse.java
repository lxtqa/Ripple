package ripple.server.tcp;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckResponse extends Message {
    private UUID uuid;
    private boolean success;

    public AckResponse() {
        super(MessageType.ACK_RESPONSE);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}