package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetClientListRequest;

/**
 * @author Zhen Tang
 */
public class GetClientListRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        GetClientListRequest getClientListRequest = (GetClientListRequest) message;
        byteBuf.writeByte(getClientListRequest.getType().getValue());
        TypeHelper.writeUuid(getClientListRequest.getUuid(), byteBuf);
        TypeHelper.writeString(getClientListRequest.getClientListSignature(), byteBuf);
    }
}