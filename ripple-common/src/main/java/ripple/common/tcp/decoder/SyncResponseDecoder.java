package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SyncResponse;

/**
 * @author Zhen Tang
 */
public class SyncResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SyncResponse syncResponse = new SyncResponse();
        syncResponse.setUuid(TypeHelper.readUuid(byteBuf));
        syncResponse.setSuccess(TypeHelper.readBoolean(byteBuf));
        return syncResponse;
    }
}
