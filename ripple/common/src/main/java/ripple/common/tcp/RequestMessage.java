package ripple.common.tcp;

import java.util.UUID;

public class RequestMessage extends Message {
    private UUID uuid;
    private byte[] payload;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
