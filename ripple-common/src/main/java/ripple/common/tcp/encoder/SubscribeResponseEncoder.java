package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SubscribeResponse;

/**
 * @author Zhen Tang
 */
public class SubscribeResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SubscribeResponse subscribeResponse = (SubscribeResponse) message;
        byteBuf.writeByte(subscribeResponse.getType().getValue());
        TypeHelper.writeUuid(subscribeResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(subscribeResponse.isSuccess(), byteBuf);
    }
}
