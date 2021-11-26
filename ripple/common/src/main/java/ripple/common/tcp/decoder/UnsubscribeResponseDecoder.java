package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.UnsubscribeResponse;

/**
 * @author Zhen Tang
 */
public class UnsubscribeResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        UnsubscribeResponse unsubscribeResponse = new UnsubscribeResponse();
        unsubscribeResponse.setUuid(TypeHelper.readUuid(byteBuf));
        unsubscribeResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return unsubscribeResponse;
    }
}
