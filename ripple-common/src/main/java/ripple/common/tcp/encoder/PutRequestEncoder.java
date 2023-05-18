package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.PutRequest;

/**
 * @author Zhen Tang
 */
public class PutRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        PutRequest putRequest = (PutRequest) message;
        byteBuf.writeByte(putRequest.getType().getValue());
        TypeHelper.writeUuid(putRequest.getUuid(), byteBuf);
        TypeHelper.writeString(putRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(putRequest.getKey(), byteBuf);
        TypeHelper.writeString(putRequest.getValue(), byteBuf);
    }
}