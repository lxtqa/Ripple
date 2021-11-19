package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDecoder extends ByteToMessageDecoder {
    private Map<MessageType, Decoder> decoders;

    private Map<MessageType, Decoder> getDecoders() {
        return decoders;
    }

    private void setDecoders(Map<MessageType, Decoder> decoders) {
        this.decoders = decoders;
    }

    public MessageDecoder() {
        this.setDecoders(new HashMap<>());
    }

    public void registerDecoder(MessageType type, Decoder decoder) {
        this.getDecoders().put(type, decoder);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        MessageType messageType = MessageType.get(byteBuf.readByte());
        Decoder decoder = this.getDecoders().get(messageType);
        if (decoder != null) {
            Message message = decoder.decode(byteBuf, messageType);
            out.add(message);
        } else {
            System.out.println("Cannot find the decoder for the message type: " + messageType);
        }
    }
}
