package ripple.common.tcp;

public class AckRequestHandler implements Handler {
    @Override
    public Message handle(Message message) {
        AckRequest ackRequest = (AckRequest) message;
        System.out.println("Handle ack request: uuid = " + ackRequest.getUuid().toString()
                + ", type = " + ackRequest.getType()
                + ", message uuid = " + ackRequest.getMessageUuid()
                + ", node id = " + ackRequest.getNodeId()
                + ", source id = " + ackRequest.getSourceId());
        AckResponse response = new AckResponse();
        response.setUuid(message.getUuid());
        response.setType(message.getType());
        response.setSuccess(true);
        return response;
    }

    @Override
    public boolean canHandle(Message message) {
        return message instanceof AckRequest
                && message.getType() == MessageType.ACK_REQUEST;
    }
}
