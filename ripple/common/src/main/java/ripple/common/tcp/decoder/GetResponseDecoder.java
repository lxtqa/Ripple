package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.GetResponse;
import ripple.common.tcp.message.GetResponseItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Zhen Tang
 */
public class GetResponseDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        GetResponse getResponse = new GetResponse();
        getResponse.setUuid(TypeHelper.readUuid(byteBuf));
        getResponse.setApplicationName(TypeHelper.readString(byteBuf));
        getResponse.setKey(TypeHelper.readString(byteBuf));
        getResponse.setItems(new ArrayList<>());
        int itemsCount = byteBuf.readInt();
        int i = 0;
        for (i = 0; i < itemsCount; i++) {
            GetResponseItem getResponseItem = new GetResponseItem();
            getResponseItem.setMessageUuid(TypeHelper.readUuid(byteBuf));
            getResponseItem.setOperationType(TypeHelper.readString(byteBuf));
            getResponseItem.setApplicationName(TypeHelper.readString(byteBuf));
            getResponseItem.setKey(TypeHelper.readString(byteBuf));
            if (getResponseItem.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
                getResponseItem.setValue(TypeHelper.readString(byteBuf));
            }
            getResponseItem.setLastUpdate(new Date(byteBuf.readLong()));
            getResponseItem.setLastUpdateServerId(byteBuf.readInt());
            getResponse.getItems().add(getResponseItem);
        }
        return getResponse;
    }
}