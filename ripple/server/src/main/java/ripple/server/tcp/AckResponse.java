package ripple.server.tcp;

import ripple.common.tcp.Message;

/**
 * @author Zhen Tang
 */
public class AckResponse extends Message {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}