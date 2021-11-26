package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.DeleteResponse;

/**
 * @author Zhen Tang
 */
public class DeleteResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setUuid(TypeHelper.readUuid(byteBuf));
        deleteResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return deleteResponse;
    }
}
