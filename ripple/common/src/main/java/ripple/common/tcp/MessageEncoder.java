package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        System.out.println("encode");

        if (message.getType() != MessageType.EMPTY) {
            String uuidString = message.getUuid().toString();
            out.writeInt(uuidString.length());
            out.writeBytes(uuidString.getBytes(StandardCharsets.UTF_8));
            out.writeByte(message.getType().getValue());
            out.writeInt(message.getPayload().length);
            out.writeBytes(message.getPayload());
        }
    }
}
