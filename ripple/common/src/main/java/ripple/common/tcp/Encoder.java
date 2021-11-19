package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

/**
 * @author Zhen Tang
 */
public interface Encoder {
    void encode(Message message, ByteBuf out);
}
