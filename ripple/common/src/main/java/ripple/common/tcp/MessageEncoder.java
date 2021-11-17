package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        if (message.getType() != MessageType.EMPTY) {
            if (message.getUuid() == null) {
                System.out.println("null uuid");
            }

            out.writeByte(message.getType().getValue());
            byte[] uuidBytes = message.getUuid().toString().getBytes(StandardCharsets.UTF_8);
            out.writeInt(uuidBytes.length);
            out.writeBytes(uuidBytes);

            out.writeInt(message.getPayload().length);
            out.writeBytes(message.getPayload());
        }
    }
}
