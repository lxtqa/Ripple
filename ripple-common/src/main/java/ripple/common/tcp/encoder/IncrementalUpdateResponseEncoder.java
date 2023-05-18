package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.IncrementalUpdateResponse;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        IncrementalUpdateResponse incrementalUpdateResponse = (IncrementalUpdateResponse) message;
        byteBuf.writeByte(incrementalUpdateResponse.getType().getValue());
        TypeHelper.writeUuid(incrementalUpdateResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(incrementalUpdateResponse.isSuccess(), byteBuf);
    }
}