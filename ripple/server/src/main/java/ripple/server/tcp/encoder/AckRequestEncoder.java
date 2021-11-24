package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.server.tcp.message.AckRequest;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class AckRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf out) {
        AckRequest ackRequest = (AckRequest) message;
        out.writeByte(ackRequest.getType().getValue());

        if (ackRequest.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = ackRequest.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);

        if (ackRequest.getMessageUuid() == null) {
            throw new NullPointerException("message.messageUuid");
        }
        byte[] messageUuidBytes = ackRequest.getMessageUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(messageUuidBytes.length);
        out.writeBytes(messageUuidBytes);

        out.writeInt(ackRequest.getSourceId());
        out.writeInt(ackRequest.getNodeId());
    }
}
