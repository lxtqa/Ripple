package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        if (message.getMessageType() != MessageTypeEnum.EMPTY) {
            out.writeInt(Constants.MAGIC_NUMBER);
            out.writeInt(Constants.PROTOCOL_VERSION);

            out.writeByte(message.getMessageType().getType());
            out.writeInt(message.getPayload().length);
            out.writeBytes(message.getPayload());
        }
    }
}
