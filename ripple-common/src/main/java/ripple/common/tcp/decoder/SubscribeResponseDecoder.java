package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SubscribeResponse;

/**
 * @author Zhen Tang
 */
public class SubscribeResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SubscribeResponse subscribeResponse = new SubscribeResponse();
        subscribeResponse.setUuid(TypeHelper.readUuid(byteBuf));
        subscribeResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return subscribeResponse;
    }
}
