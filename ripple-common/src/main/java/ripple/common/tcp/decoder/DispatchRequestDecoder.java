// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.DispatchRequest;

import java.util.Date;

/**
 * @author Zhen Tang
 */
public class DispatchRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        DispatchRequest dispatchRequest = new DispatchRequest();
        dispatchRequest.setUuid(TypeHelper.readUuid(byteBuf));
        dispatchRequest.setClientListSignature(TypeHelper.readString(byteBuf));
        dispatchRequest.setMessageUuid(TypeHelper.readUuid(byteBuf));
        dispatchRequest.setOperationType(TypeHelper.readString(byteBuf));
        dispatchRequest.setApplicationName(TypeHelper.readString(byteBuf));
        dispatchRequest.setKey(TypeHelper.readString(byteBuf));
        if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            dispatchRequest.setValue(TypeHelper.readString(byteBuf));
        } else if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            dispatchRequest.setBaseMessageUuid(TypeHelper.readUuid(byteBuf));
            dispatchRequest.setAtomicOperation(TypeHelper.readString(byteBuf));
            dispatchRequest.setValue(TypeHelper.readString(byteBuf));
        }
        dispatchRequest.setLastUpdate(new Date(byteBuf.readLong()));
        dispatchRequest.setLastUpdateServerId(byteBuf.readInt());
        return dispatchRequest;
    }
}