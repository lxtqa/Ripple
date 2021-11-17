package ripple.common.tcp;

public class PongMessageResolver implements Resolver {

    @Override
    public boolean support(Message message) {
        return message.getType() == MessageType.PONG;
    }

    @Override
    public Message resolve(Message message) {
        System.out.println("Receive pong message: " + System.currentTimeMillis());
        Message empty = new Message();
        empty.setType(MessageType.EMPTY);
        return empty;
    }
}
