// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.DispatchRequest;

/**
 * @author Zhen Tang
 */
public class DispatchRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        DispatchRequest dispatchRequest = (DispatchRequest) message;
        byteBuf.writeByte(dispatchRequest.getType().getValue());
        TypeHelper.writeUuid(dispatchRequest.getUuid(), byteBuf);
        TypeHelper.writeString(dispatchRequest.getClientListSignature(), byteBuf);
        TypeHelper.writeUuid(dispatchRequest.getMessageUuid(), byteBuf);
        TypeHelper.writeString(dispatchRequest.getOperationType(), byteBuf);
        TypeHelper.writeString(dispatchRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(dispatchRequest.getKey(), byteBuf);
        if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            TypeHelper.writeString(dispatchRequest.getValue(), byteBuf);
        } else if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            TypeHelper.writeUuid(dispatchRequest.getBaseMessageUuid(), byteBuf);
            TypeHelper.writeString(dispatchRequest.getAtomicOperation(), byteBuf);
            TypeHelper.writeString(dispatchRequest.getValue(), byteBuf);
        }
        byteBuf.writeLong(dispatchRequest.getLastUpdate().getTime());
        byteBuf.writeInt(dispatchRequest.getLastUpdateServerId());
    }
}