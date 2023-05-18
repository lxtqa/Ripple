package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.DispatchResponse;

/**
 * @author Zhen Tang
 */
public class DispatchResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        DispatchResponse dispatchResponse = (DispatchResponse) message;
        byteBuf.writeByte(dispatchResponse.getType().getValue());
        TypeHelper.writeUuid(dispatchResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(dispatchResponse.isSuccess(), byteBuf);
    }
}
