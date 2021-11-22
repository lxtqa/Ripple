package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponse extends Message {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HeartbeatResponse() {
        this.setType(MessageType.HEARTBEAT_RESPONSE);
    }
}
