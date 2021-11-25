package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetResponse;
import ripple.common.tcp.message.GetResponseItem;

/**
 * @author Zhen Tang
 */
public class GetResponseEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        GetResponse getResponse = (GetResponse) message;
        byteBuf.writeByte(getResponse.getType().getValue());
        TypeHelper.writeUuid(getResponse.getUuid(), byteBuf);
        TypeHelper.writeString(getResponse.getApplicationName(), byteBuf);
        TypeHelper.writeString(getResponse.getKey(), byteBuf);
        int itemsCount = getResponse.getItems().size();
        byteBuf.writeInt(itemsCount);
        int i = 0;
        for (i = 0; i < itemsCount; i++) {
            GetResponseItem getResponseItem = getResponse.getItems().get(i);
            TypeHelper.writeUuid(getResponseItem.getMessageUuid(), byteBuf);
            TypeHelper.writeString(getResponseItem.getOperationType(), byteBuf);
            TypeHelper.writeString(getResponseItem.getApplicationName(), byteBuf);
            TypeHelper.writeString(getResponseItem.getKey(), byteBuf);
            if (getResponseItem.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
                TypeHelper.writeString(getResponseItem.getValue(), byteBuf);
            }
            byteBuf.writeLong(getResponseItem.getLastUpdate().getTime());
            byteBuf.writeInt(getResponseItem.getLastUpdateServerId());
        }
    }
}