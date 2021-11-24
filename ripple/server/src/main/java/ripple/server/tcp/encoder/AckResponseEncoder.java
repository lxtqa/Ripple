package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.server.tcp.message.AckResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class AckResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf out) {
        AckResponse ackResponse = (AckResponse) message;
        out.writeByte(ackResponse.getType().getValue());

        if (ackResponse.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = ackResponse.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);
        out.writeByte(ackResponse.isSuccess() ? (byte) 1 : (byte) 0);
    }
}
