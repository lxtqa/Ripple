package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.DeleteRequest;

/**
 * @author Zhen Tang
 */
public class DeleteRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        DeleteRequest deleteRequest = (DeleteRequest) message;
        byteBuf.writeByte(deleteRequest.getType().getValue());
        TypeHelper.writeUuid(deleteRequest.getUuid(), byteBuf);
        TypeHelper.writeString(deleteRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(deleteRequest.getKey(), byteBuf);
    }
}