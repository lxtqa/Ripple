package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequest extends Message {
    public HeartbeatRequest() {
        this.setType(MessageType.HEARTBEAT_REQUEST);
    }
}
