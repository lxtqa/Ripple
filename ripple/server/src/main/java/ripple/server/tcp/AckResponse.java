package ripple.server.tcp;

import ripple.common.tcp.Message;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckResponse extends Message {
    private UUID uuid;
    private boolean success;

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