package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.PutResponse;

/**
 * @author Zhen Tang
 */
public class PutResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        PutResponse putResponse = new PutResponse();
        putResponse.setUuid(TypeHelper.readUuid(byteBuf));
        putResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return putResponse;
    }
}
