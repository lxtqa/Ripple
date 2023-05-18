package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.IncrementalUpdateRequest;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        IncrementalUpdateRequest incrementalUpdateRequest = new IncrementalUpdateRequest();
        incrementalUpdateRequest.setUuid(TypeHelper.readUuid(byteBuf));
        incrementalUpdateRequest.setApplicationName(TypeHelper.readString(byteBuf));
        incrementalUpdateRequest.setKey(TypeHelper.readString(byteBuf));
        incrementalUpdateRequest.setBaseMessageUuid(TypeHelper.readUuid(byteBuf));
        incrementalUpdateRequest.setAtomicOperation(TypeHelper.readString(byteBuf));
        incrementalUpdateRequest.setValue(TypeHelper.readString(byteBuf));
        return incrementalUpdateRequest;
    }
}