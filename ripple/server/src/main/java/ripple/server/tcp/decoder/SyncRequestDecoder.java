package ripple.server.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SyncRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class SyncRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SyncRequest syncRequest = new SyncRequest();

        int uuidSize = byteBuf.readInt();
        byte[] uuidBytes = new byte[uuidSize];
        byteBuf.readBytes(uuidBytes);
        syncRequest.setUuid(UUID.fromString(new String(uuidBytes, StandardCharsets.UTF_8)));

        int messageUuidSize = byteBuf.readInt();
        byte[] messageUuidBytes = new byte[messageUuidSize];
        byteBuf.readBytes(messageUuidBytes);
        syncRequest.setMessageUuid(UUID.fromString(new String(messageUuidBytes, StandardCharsets.UTF_8)));

        int operationTypeSize = byteBuf.readInt();
        byte[] operationTypeBytes = new byte[operationTypeSize];
        byteBuf.readBytes(operationTypeBytes);
        syncRequest.setOperationType(new String(operationTypeBytes, StandardCharsets.UTF_8));

        int applicationNameSize = byteBuf.readInt();
        byte[] applicationNameBytes = new byte[applicationNameSize];
        byteBuf.readBytes(applicationNameBytes);
        syncRequest.setApplicationName(new String(applicationNameBytes, StandardCharsets.UTF_8));

        int keySize = byteBuf.readInt();
        byte[] keyBytes = new byte[keySize];
        byteBuf.readBytes(keyBytes);
        syncRequest.setKey(new String(keyBytes, StandardCharsets.UTF_8));

        int valueSize = byteBuf.readInt();
        byte[] valueBytes = new byte[valueSize];
        byteBuf.readBytes(valueBytes);
        syncRequest.setValue(new String(valueBytes, StandardCharsets.UTF_8));

        syncRequest.setLastUpdate(new Date(byteBuf.readLong()));
        syncRequest.setLastUpdateServerId(byteBuf.readInt());

        return syncRequest;
    }
}