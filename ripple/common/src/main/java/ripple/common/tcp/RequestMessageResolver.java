package ripple.common.tcp;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestMessageResolver implements Resolver {

    private static final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public boolean support(Message message) {
        return message.getType() == MessageType.REQUEST;
    }

    @Override
    public Message resolve(Message message) {
        RequestMessage requestMessage = (RequestMessage) message;
        int index = counter.getAndIncrement();
        System.out.println(index + ". Receive request: " + new String(requestMessage.getPayload(), StandardCharsets.UTF_8)
                + ", uuid = " + requestMessage.getUuid().toString());

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setType(MessageType.RESPONSE);
        responseMessage.setUuid(requestMessage.getUuid());
        responseMessage.setPayload("Response.".getBytes(StandardCharsets.UTF_8));
        return responseMessage;
    }
}