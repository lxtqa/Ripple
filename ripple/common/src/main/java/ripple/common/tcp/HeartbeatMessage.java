package ripple.common.tcp;

public class HeartbeatMessage extends Message {

    public HeartbeatMessage() {
        super(MessageType.HEARTBEAT);
    }
}
