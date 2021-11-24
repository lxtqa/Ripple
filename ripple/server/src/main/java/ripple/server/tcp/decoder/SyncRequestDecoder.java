package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SyncRequest;

import java.util.Date;

/**
 * @author Zhen Tang
 */
public class SyncRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUuid(TypeHelper.readUuid(byteBuf));
        syncRequest.setMessageUuid(TypeHelper.readUuid(byteBuf));
        syncRequest.setOperationType(TypeHelper.readString(byteBuf));
        syncRequest.setApplicationName(TypeHelper.readString(byteBuf));
        syncRequest.setKey(TypeHelper.readString(byteBuf));
        if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            syncRequest.setValue(TypeHelper.readString(byteBuf));
        }
        syncRequest.setLastUpdate(new Date(byteBuf.readLong()));
        syncRequest.setLastUpdateServerId(byteBuf.readInt());
        return syncRequest;
    }
}