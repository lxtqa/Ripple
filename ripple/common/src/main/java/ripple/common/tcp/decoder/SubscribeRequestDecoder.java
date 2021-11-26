package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SubscribeRequest;

/**
 * @author Zhen Tang
 */
public class SubscribeRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setUuid(TypeHelper.readUuid(byteBuf));
        subscribeRequest.setApplicationName(TypeHelper.readString(byteBuf));
        subscribeRequest.setKey(TypeHelper.readString(byteBuf));
        subscribeRequest.setCallbackAddress(TypeHelper.readString(byteBuf));
        subscribeRequest.setCallbackPort(byteBuf.readInt());
        return subscribeRequest;
    }
}