package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SyncResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class SyncResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SyncResponse syncResponse = new SyncResponse();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        syncResponse.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        byte success = byteBuf.readByte();
        syncResponse.setSuccess(success == 1 ? true : false);

        return syncResponse;
    }
}
