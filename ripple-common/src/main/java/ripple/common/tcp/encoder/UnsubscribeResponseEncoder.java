package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.UnsubscribeResponse;

/**
 * @author Zhen Tang
 */
public class UnsubscribeResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        UnsubscribeResponse unsubscribeResponse = (UnsubscribeResponse) message;
        byteBuf.writeByte(unsubscribeResponse.getType().getValue());
        TypeHelper.writeUuid(unsubscribeResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(unsubscribeResponse.isSuccess(), byteBuf);
    }
}
