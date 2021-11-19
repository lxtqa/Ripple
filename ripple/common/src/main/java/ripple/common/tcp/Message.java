package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public class Message {
    private MessageType type;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    protected Message(MessageType type) {
        this.setType(type);
    }
}
