package ripple.common.tcp;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestMessageResolver implements Resolver {

    private static final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public boolean support(Message message) {
        return message.getMessageType() == MessageTypeEnum.REQUEST;
    }

    @Override
    public Message resolve(Message message) {
        int index = counter.getAndIncrement();
        System.out.println(index + ". Receive request: " + new String(message.getPayload(), StandardCharsets.UTF_8));

        Message response = new Message();
        response.setMessageType(MessageTypeEnum.RESPONSE);
        response.setPayload("Response.".getBytes(StandardCharsets.UTF_8));
        return response;
    }
}