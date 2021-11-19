package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.HashMap;
import java.util.Map;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    private Map<MessageType, Encoder> encoders;

    private Map<MessageType, Encoder> getEncoders() {
        return encoders;
    }

    private void setEncoders(Map<MessageType, Encoder> encoders) {
        this.encoders = encoders;
    }

    public MessageEncoder() {
        this.setEncoders(new HashMap<>());
    }

    public void registerEncoder(MessageType type, Encoder encoder) {
        this.getEncoders().put(type, encoder);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        Encoder encoder = this.getEncoders().get(message.getType());
        if (encoder != null) {
            encoder.encode(message, out);
        } else {
            System.out.println("Cannot find the encoder for the message type: " + message.getType());
        }
    }
}
