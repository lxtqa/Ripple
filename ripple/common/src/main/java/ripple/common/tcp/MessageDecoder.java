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
        Message message = new Message();
        message.setType(MessageType.get(byteBuf.readByte()));

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
