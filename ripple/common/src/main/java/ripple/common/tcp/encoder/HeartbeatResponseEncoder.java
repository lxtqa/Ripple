package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseEncoder implements Encoder {
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
}
