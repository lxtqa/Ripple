package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestEncoder implements Encoder {
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
}
