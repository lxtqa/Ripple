package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.PutResponse;

/**
 * @author Zhen Tang
 */
public class PutResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        PutResponse putResponse = (PutResponse) message;
        byteBuf.writeByte(putResponse.getType().getValue());
        TypeHelper.writeUuid(putResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(putResponse.isSuccess(), byteBuf);
    }
}
