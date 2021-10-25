package ripple.test.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        Message message = new Message();
        message.setMagicNumber(byteBuf.readInt());
        message.setProtocolVersion(byteBuf.readInt());

        message.setMessageType(MessageTypeEnum.get(byteBuf.readByte()));    // 读取当前的消息类型
        int payloadSize = byteBuf.readInt();
        byte[] payload = new byte[payloadSize];
        byteBuf.readBytes(payload);

        message.setPayload(payload);
        out.add(message);
    }
}