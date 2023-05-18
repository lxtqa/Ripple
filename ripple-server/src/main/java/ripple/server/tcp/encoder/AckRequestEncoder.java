package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.server.tcp.message.AckRequest;

/**
 * @author Zhen Tang
 */
public class AckRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        AckRequest ackRequest = (AckRequest) message;
        byteBuf.writeByte(ackRequest.getType().getValue());
        TypeHelper.writeUuid(ackRequest.getUuid(), byteBuf);
        TypeHelper.writeUuid(ackRequest.getMessageUuid(), byteBuf);
        byteBuf.writeInt(ackRequest.getSourceId());
        byteBuf.writeInt(ackRequest.getNodeId());
    }
}
