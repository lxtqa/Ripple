package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateResponse extends Message {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public IncrementalUpdateResponse() {
        this.setType(MessageType.INCREMENTAL_UPDATE_RESPONSE);
    }
}
