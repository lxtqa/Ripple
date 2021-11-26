package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.UnsubscribeRequest;

/**
 * @author Zhen Tang
 */
public class UnsubscribeRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setUuid(TypeHelper.readUuid(byteBuf));
        unsubscribeRequest.setApplicationName(TypeHelper.readString(byteBuf));
        unsubscribeRequest.setKey(TypeHelper.readString(byteBuf));
        unsubscribeRequest.setCallbackAddress(TypeHelper.readString(byteBuf));
        unsubscribeRequest.setCallbackPort(byteBuf.readInt());
        return unsubscribeRequest;
    }
}