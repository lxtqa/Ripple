package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestProcessor implements Encoder, Decoder, Handler {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setType(messageType);

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        heartbeatRequest.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        return heartbeatRequest;
    }

    @Override
    public void encode(Message message, ByteBuf out) {
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        out.writeByte(heartbeatRequest.getType().getValue());

        if (heartbeatRequest.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = heartbeatRequest.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);
    }

    @Override
    public Message handle(Message message) {
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        System.out.println("Receive heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        heartbeatResponse.setType(MessageType.HEARTBEAT_RESPONSE);
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);
        return heartbeatResponse;
    }
}
