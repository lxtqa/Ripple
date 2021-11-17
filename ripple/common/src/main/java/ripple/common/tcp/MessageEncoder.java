package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        if (message.getType() == MessageType.REQUEST) {
            RequestMessage requestMessage = (RequestMessage) message;
            if (requestMessage.getUuid() == null) {
                throw new NullPointerException("message.uuid");
            }

            out.writeByte(message.getType().getValue());
            byte[] uuidBytes = requestMessage.getUuid().toString().getBytes(StandardCharsets.UTF_8);
            out.writeInt(uuidBytes.length);
            out.writeBytes(uuidBytes);

            out.writeInt(requestMessage.getPayload().length);
            out.writeBytes(requestMessage.getPayload());
        } else if (message.getType() == MessageType.RESPONSE) {
            ResponseMessage responseMessage = (ResponseMessage) message;
            if (responseMessage.getUuid() == null) {
                throw new NullPointerException("message.uuid");
            }

            out.writeByte(message.getType().getValue());
            byte[] uuidBytes = responseMessage.getUuid().toString().getBytes(StandardCharsets.UTF_8);
            out.writeInt(uuidBytes.length);
            out.writeBytes(uuidBytes);

            out.writeInt(responseMessage.getPayload().length);
            out.writeBytes(responseMessage.getPayload());
        }
    }
}
