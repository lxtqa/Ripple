package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        MessageType messageType = MessageType.get(byteBuf.readByte());

        if (messageType == MessageType.REQUEST) {
            RequestMessage message = new RequestMessage();
            message.setType(messageType);
            int uuidSize = byteBuf.readInt();
            byte[] uuidBytes = new byte[uuidSize];
            byteBuf.readBytes(uuidBytes);
            message.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

            int payloadSize = byteBuf.readInt();
            byte[] payload = new byte[payloadSize];
            byteBuf.readBytes(payload);
            message.setPayload(payload);

            out.add(message);
        } else if (messageType == MessageType.RESPONSE) {
            ResponseMessage message = new ResponseMessage();
            message.setType(messageType);
            int uuidSize = byteBuf.readInt();
            byte[] uuidBytes = new byte[uuidSize];
            byteBuf.readBytes(uuidBytes);
            message.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

            int payloadSize = byteBuf.readInt();
            byte[] payload = new byte[payloadSize];
            byteBuf.readBytes(payload);
            message.setPayload(payload);

            out.add(message);
        }
    }
}
