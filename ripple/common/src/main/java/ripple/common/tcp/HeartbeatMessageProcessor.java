package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

public class HeartbeatMessageProcessor implements Encoder, Decoder, Handler {

    @Override
    public void encode(Message message, ByteBuf out) {
        HeartbeatMessage heartbeatMessage = (HeartbeatMessage) message;
        out.writeByte(heartbeatMessage.getType().getValue());

//        ResponseMessage responseMessage = (ResponseMessage) message;
//        if (responseMessage.getUuid() == null) {
//            throw new NullPointerException("message.uuid");
//        }

//        byte[] uuidBytes = responseMessage.getUuid().toString().getBytes(StandardCharsets.UTF_8);
//        out.writeInt(uuidBytes.length);
//        out.writeBytes(uuidBytes);
//
//        out.writeInt(responseMessage.getPayload().length);
//        out.writeBytes(responseMessage.getPayload());
    }

    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatMessage heartbeatMessage = new HeartbeatMessage();
        heartbeatMessage.setType(messageType);
        return heartbeatMessage;

//        int uuidSize = byteBuf.readInt();
//        byte[] uuidBytes = new byte[uuidSize];
//        byteBuf.readBytes(uuidBytes);
//        responseMessage.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));
//
//        int payloadSize = byteBuf.readInt();
//        byte[] payload = new byte[payloadSize];
//        byteBuf.readBytes(payload);
//        responseMessage.setPayload(payload);
//        return responseMessage;
    }

    @Override
    public Message handle(Message message) {
        HeartbeatMessage heartbeatMessage = (HeartbeatMessage) message;
        System.out.println("Receive heartbeat message.");
        return null;
    }
}
