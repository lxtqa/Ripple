package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

public class UnsubscribeResponse extends Message {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UnsubscribeResponse() {
        this.setType(MessageType.UNSUBSCRIBE_RESPONSE);
    }
}
