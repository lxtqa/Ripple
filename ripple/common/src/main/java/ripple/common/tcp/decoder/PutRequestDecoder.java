package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.PutRequest;

/**
 * @author Zhen Tang
 */
public class PutRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        PutRequest putRequest = new PutRequest();
        putRequest.setUuid(TypeHelper.readUuid(byteBuf));
        putRequest.setApplicationName(TypeHelper.readString(byteBuf));
        putRequest.setKey(TypeHelper.readString(byteBuf));
        putRequest.setValue(TypeHelper.readString(byteBuf));
        return putRequest;
    }
}