package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseProcessor implements Encoder, Decoder, Handler {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        heartbeatResponse.setType(messageType);

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        heartbeatResponse.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        byte success = byteBuf.readByte();
        heartbeatResponse.setSuccess(success == 1 ? true : false);

        return heartbeatResponse;
    }

    @Override
    public void encode(Message message, ByteBuf out) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        out.writeByte(heartbeatResponse.getType().getValue());

        if (heartbeatResponse.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = heartbeatResponse.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);
        out.writeByte(heartbeatResponse.isSuccess() ? (byte) 1 : (byte) 0);
    }

    @Override
    public Message handle(Message message) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        System.out.println("Receive heartbeat response. uuid = "
                + heartbeatResponse.getUuid().toString()
                + ", success = " + heartbeatResponse.isSuccess());
        return null;
    }
}
