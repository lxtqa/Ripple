package ripple.common.tcp;

import java.util.UUID;

public class PingMessageResolver implements Resolver {

    @Override
    public boolean support(Message message) {
        return message.getType() == MessageType.PING;
    }

    @Override
    public Message resolve(Message message) {
        System.out.println("Receive ping message: " + System.currentTimeMillis());
        Message pong = new Message();
        pong.setType(MessageType.PONG);
        message.setUuid(UUID.randomUUID());
        return pong;
    }
}
