package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SubscribeRequest;

/**
 * @author Zhen Tang
 */
public class SubscribeRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SubscribeRequest subscribeRequest = (SubscribeRequest) message;
        byteBuf.writeByte(subscribeRequest.getType().getValue());
        TypeHelper.writeUuid(subscribeRequest.getUuid(), byteBuf);
        TypeHelper.writeString(subscribeRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(subscribeRequest.getKey(), byteBuf);
        TypeHelper.writeString(subscribeRequest.getCallbackAddress(), byteBuf);
        byteBuf.writeInt(subscribeRequest.getCallbackPort());
    }
}