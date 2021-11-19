package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

/**
 * @author Zhen Tang
 */
public interface Decoder {
    Message decode(ByteBuf byteBuf, MessageType messageType);
}
