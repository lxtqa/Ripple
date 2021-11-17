package ripple.common.tcp;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseMessageResolver implements Resolver {

    private static final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public boolean support(Message message) {
        return message.getType() == MessageType.RESPONSE;
    }

    @Override
    public Message resolve(Message message) {
        int index = counter.getAndIncrement();
        System.out.println(index + ". receive response: " + new String(message.getPayload(), StandardCharsets.UTF_8));

        Message empty = new Message();
        empty.setType(MessageType.EMPTY);
        return empty;
    }
}