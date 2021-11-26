package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.UnsubscribeRequest;

/**
 * @author Zhen Tang
 */
public class UnsubscribeRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        UnsubscribeRequest unsubscribeRequest = (UnsubscribeRequest) message;
        byteBuf.writeByte(unsubscribeRequest.getType().getValue());
        TypeHelper.writeUuid(unsubscribeRequest.getUuid(), byteBuf);
        TypeHelper.writeString(unsubscribeRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(unsubscribeRequest.getKey(), byteBuf);
        TypeHelper.writeString(unsubscribeRequest.getCallbackAddress(), byteBuf);
        byteBuf.writeInt(unsubscribeRequest.getCallbackPort());
    }
}