package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.server.tcp.message.AckRequest;

/**
 * @author Zhen Tang
 */
public class AckRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        AckRequest ackRequest = new AckRequest();
        ackRequest.setUuid(TypeHelper.readUuid(byteBuf));
        ackRequest.setMessageUuid(TypeHelper.readUuid(byteBuf));
        ackRequest.setSourceId(byteBuf.readInt());
        ackRequest.setNodeId(byteBuf.readInt());
        return ackRequest;
    }
}