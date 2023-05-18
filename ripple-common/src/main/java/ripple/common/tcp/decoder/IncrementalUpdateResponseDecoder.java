package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.IncrementalUpdateResponse;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        IncrementalUpdateResponse incrementalUpdateResponse = new IncrementalUpdateResponse();
        incrementalUpdateResponse.setUuid(TypeHelper.readUuid(byteBuf));
        incrementalUpdateResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return incrementalUpdateResponse;
    }
}
