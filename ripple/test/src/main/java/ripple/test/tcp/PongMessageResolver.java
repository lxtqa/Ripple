package ripple.test.tcp;

public class PongMessageResolver implements Resolver {

    @Override
    public boolean support(Message message) {
        return message.getMessageType() == MessageTypeEnum.PONG;
    }

    @Override
    public Message resolve(Message message) {
        System.out.println("Receive pong message: " + System.currentTimeMillis());
        Message empty = new Message();
        empty.setMessageType(MessageTypeEnum.EMPTY);
        return empty;
    }
}