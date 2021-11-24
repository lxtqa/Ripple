package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class SyncResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf out) {
        SyncResponse syncResponse = (SyncResponse) message;
        out.writeByte(syncResponse.getType().getValue());

        if (syncResponse.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = syncResponse.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);
        out.writeByte(syncResponse.isSuccess() ? (byte) 1 : (byte) 0);
    }
}
