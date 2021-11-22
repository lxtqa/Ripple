package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public enum MessageType {
    HEARTBEAT_REQUEST((byte) 0),
    HEARTBEAT_RESPONSE((byte) 1),
    ACK_REQUEST((byte) 2),
    ACK_RESPONSE((byte) 3),
    SYNC_REQUEST((byte) 4),
    SYNC_RESPONSE((byte) 5);

    private byte value;

    public byte getValue() {
        return value;
    }

    private void setValue(byte value) {
        this.value = value;
    }

    MessageType(byte value) {
        this.setValue(value);
    }

    public static MessageType get(byte type) {
        for (MessageType elem : values()) {
            if (elem.getValue() == type) {
                return elem;
            }
        }
        throw new RuntimeException("Unsupported type: " + type);
    }
}
