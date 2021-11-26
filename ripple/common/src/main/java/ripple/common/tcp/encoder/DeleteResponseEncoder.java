package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.DeleteResponse;

/**
 * @author Zhen Tang
 */
public class DeleteResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        DeleteResponse deleteResponse = (DeleteResponse) message;
        byteBuf.writeByte(deleteResponse.getType().getValue());
        TypeHelper.writeUuid(deleteResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(deleteResponse.isSuccess(), byteBuf);
    }
}
