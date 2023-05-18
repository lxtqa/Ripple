package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.GetRequest;

/**
 * @author Zhen Tang
 */
public class GetRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        GetRequest getRequest = new GetRequest();
        getRequest.setUuid(TypeHelper.readUuid(byteBuf));
        getRequest.setApplicationName(TypeHelper.readString(byteBuf));
        getRequest.setKey(TypeHelper.readString(byteBuf));
        return getRequest;
    }
}