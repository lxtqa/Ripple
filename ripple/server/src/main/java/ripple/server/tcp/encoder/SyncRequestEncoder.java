package ripple.server.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncRequest;

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class SyncRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf out) {
        SyncRequest syncRequest = (SyncRequest) message;
        out.writeByte(syncRequest.getType().getValue());

        if (syncRequest.getUuid() == null) {
            throw new NullPointerException("message.uuid");
        }
        byte[] uuidBytes = syncRequest.getUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(uuidBytes.length);
        out.writeBytes(uuidBytes);

        if (syncRequest.getMessageUuid() == null) {
            throw new NullPointerException("message.messageUuid");
        }
        byte[] messageUuidBytes = syncRequest.getMessageUuid().toString().getBytes(StandardCharsets.UTF_8);
        out.writeInt(messageUuidBytes.length);
        out.writeBytes(messageUuidBytes);

        if (syncRequest.getOperationType() == null) {
            throw new NullPointerException("message.operationType");
        }
        byte[] operationTypeBytes = syncRequest.getOperationType().getBytes(StandardCharsets.UTF_8);
        out.writeInt(operationTypeBytes.length);
        out.writeBytes(operationTypeBytes);

        if (syncRequest.getApplicationName() == null) {
            throw new NullPointerException("message.applicationName");
        }
        byte[] applicationNameBytes = syncRequest.getApplicationName().getBytes(StandardCharsets.UTF_8);
        out.writeInt(applicationNameBytes.length);
        out.writeBytes(applicationNameBytes);

        if (syncRequest.getKey() == null) {
            throw new NullPointerException("message.key");
        }
        byte[] keyBytes = syncRequest.getKey().getBytes(StandardCharsets.UTF_8);
        out.writeInt(keyBytes.length);
        out.writeBytes(keyBytes);

        if (syncRequest.getValue() == null) {
            throw new NullPointerException("message.value");
        }
        byte[] valueBytes = syncRequest.getValue().getBytes(StandardCharsets.UTF_8);
        out.writeInt(valueBytes.length);
        out.writeBytes(valueBytes);

        out.writeLong(syncRequest.getLastUpdate().getTime());
        out.writeInt(syncRequest.getLastUpdateServerId());
    }
}