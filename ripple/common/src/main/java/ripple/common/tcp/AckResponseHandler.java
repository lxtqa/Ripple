package ripple.common.tcp;

import java.util.UUID;

public class AckResponseHandler implements Handler {
    @Override
    public Message handle(Message message) {
        AckResponse response = (AckResponse) message;
        System.out.println(response.isSuccess());
        Message ret = new Message();
        ret.setUuid(UUID.randomUUID());
        ret.setType(MessageType.EMPTY);
        return ret;
    }

    @Override
    public boolean canHandle(Message message) {
        return message instanceof AckRequest
                && message.getType() == MessageType.ACK_RESPONSE;
    }
}
