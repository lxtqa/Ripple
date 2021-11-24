package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.server.tcp.message.AckRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        AckRequest ackRequest = new AckRequest();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        ackRequest.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        int messageUuidSize = byteBuf.readInt();
        byte[] messageUuidBytes = new byte[messageUuidSize];
        byteBuf.readBytes(messageUuidBytes);
        ackRequest.setMessageUuid(UUID.fromString(new String(messageUuidBytes, StandardCharsets.UTF_8)));

        ackRequest.setSourceId(byteBuf.readInt());
        ackRequest.setNodeId(byteBuf.readInt());

        return ackRequest;
    }
}