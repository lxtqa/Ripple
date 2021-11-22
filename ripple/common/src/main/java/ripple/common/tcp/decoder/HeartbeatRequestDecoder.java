package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        heartbeatRequest.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        return heartbeatRequest;
    }
}
