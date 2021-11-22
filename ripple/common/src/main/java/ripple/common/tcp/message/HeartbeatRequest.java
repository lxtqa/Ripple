package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequest extends Message {
    public HeartbeatRequest() {
        this.setType(MessageType.HEARTBEAT_REQUEST);
    }
}
