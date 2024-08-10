package ripple.common.tcp.message;

import ripple.common.tcp.MessageType;

public enum Result {
    SUCCESS((byte) 0),
    DUPLICATED((byte) 1);

    private byte value;

    public byte getValue() {
        return value;
    }

    private void setValue(byte value) {
        this.value = value;
    }

    Result(byte value) {
        this.setValue(value);
    }

    public static Result get(byte type) {
        for (Result elem : values()) {
            if (elem.getValue() == type) {
                return elem;
            }
        }
        throw new RuntimeException("Unsupported type: " + type);
    }
}
