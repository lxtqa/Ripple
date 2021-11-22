package ripple.server.tcp;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class AckResponse extends Message {
    private boolean success;

    public AckResponse() {
        this.setType(MessageType.ACK_RESPONSE);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}