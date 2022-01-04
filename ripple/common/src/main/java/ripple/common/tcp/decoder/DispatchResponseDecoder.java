package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.DispatchResponse;

/**
 * @author Zhen Tang
 */
public class DispatchResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        DispatchResponse dispatchResponse = new DispatchResponse();
        dispatchResponse.setUuid(TypeHelper.readUuid(byteBuf));
        dispatchResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return dispatchResponse;
    }
}
