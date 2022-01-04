package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.GetClientListRequest;

/**
 * @author Zhen Tang
 */
public class GetClientListRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        GetClientListRequest getClientListRequest = new GetClientListRequest();
        getClientListRequest.setUuid(TypeHelper.readUuid(byteBuf));
        getClientListRequest.setClientListSignature(TypeHelper.readString(byteBuf));
        return getClientListRequest;
    }
}