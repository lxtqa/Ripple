package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.IncrementalUpdateRequest;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        IncrementalUpdateRequest incrementalUpdateRequest = (IncrementalUpdateRequest) message;
        byteBuf.writeByte(incrementalUpdateRequest.getType().getValue());
        TypeHelper.writeUuid(incrementalUpdateRequest.getUuid(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getKey(), byteBuf);
        TypeHelper.writeUuid(incrementalUpdateRequest.getBaseMessageUuid(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getAtomicOperation(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getValue(), byteBuf);
    }
}