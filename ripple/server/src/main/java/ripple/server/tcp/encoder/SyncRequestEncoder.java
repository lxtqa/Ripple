package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncRequest;

/**
 * @author Zhen Tang
 */
public class SyncRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SyncRequest syncRequest = (SyncRequest) message;
        byteBuf.writeByte(syncRequest.getType().getValue());
        TypeHelper.writeUuid(syncRequest.getUuid(), byteBuf);
        TypeHelper.writeUuid(syncRequest.getMessageUuid(), byteBuf);
        TypeHelper.writeString(syncRequest.getOperationType(), byteBuf);
        TypeHelper.writeString(syncRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(syncRequest.getKey(), byteBuf);
        TypeHelper.writeString(syncRequest.getValue(), byteBuf);
        byteBuf.writeLong(syncRequest.getLastUpdate().getTime());
        byteBuf.writeInt(syncRequest.getLastUpdateServerId());
    }
}