package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.DeleteRequest;

/**
 * @author Zhen Tang
 */
public class DeleteRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setUuid(TypeHelper.readUuid(byteBuf));
        deleteRequest.setApplicationName(TypeHelper.readString(byteBuf));
        deleteRequest.setKey(TypeHelper.readString(byteBuf));
        return deleteRequest;
    }
}