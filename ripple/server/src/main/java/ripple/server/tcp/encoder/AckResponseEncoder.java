package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.server.tcp.message.AckResponse;

/**
 * @author Zhen Tang
 */
public class AckResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        AckResponse ackResponse = (AckResponse) message;
        byteBuf.writeByte(ackResponse.getType().getValue());
        TypeHelper.writeUuid(ackResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(ackResponse.isSuccess(), byteBuf);
    }
}
