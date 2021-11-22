package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.HeartbeatResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        heartbeatResponse.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        byte success = byteBuf.readByte();
        heartbeatResponse.setSuccess(success == 1 ? true : false);

        return heartbeatResponse;
    }
}
