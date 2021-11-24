package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.server.tcp.message.AckResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        AckResponse ackResponse = new AckResponse();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        ackResponse.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        byte success = byteBuf.readByte();
        ackResponse.setSuccess(success == 1 ? true : false);

        return ackResponse;
    }
}
