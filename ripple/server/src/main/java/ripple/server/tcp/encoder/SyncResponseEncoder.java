package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncResponse;

/**
 * @author Zhen Tang
 */
public class SyncResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SyncResponse syncResponse = (SyncResponse) message;
        byteBuf.writeByte(syncResponse.getType().getValue());
        TypeHelper.writeUuid(syncResponse.getUuid(), byteBuf);
        TypeHelper.writeBoolean(syncResponse.isSuccess(), byteBuf);
    }
}
