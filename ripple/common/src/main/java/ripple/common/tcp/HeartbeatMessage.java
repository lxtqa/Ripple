package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public class HeartbeatMessage extends Message {
    public HeartbeatMessage() {
        super(MessageType.HEARTBEAT);
    }
}
