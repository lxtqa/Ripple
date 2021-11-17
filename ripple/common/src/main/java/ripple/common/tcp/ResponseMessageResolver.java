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
        ResponseMessage responseMessage = (ResponseMessage) message;
        int index = counter.getAndIncrement();
        System.out.println(index + ". receive response: " + new String(responseMessage.getPayload(), StandardCharsets.UTF_8)
                + ", uuid = " + responseMessage.getUuid().toString());
        return null;
    }
}