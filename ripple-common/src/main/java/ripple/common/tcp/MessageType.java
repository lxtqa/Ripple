// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public enum MessageType {
    HEARTBEAT_REQUEST((byte) 0),
    HEARTBEAT_RESPONSE((byte) 1),
    ACK_REQUEST((byte) 2),
    ACK_RESPONSE((byte) 3),
    SYNC_REQUEST((byte) 4),
    SYNC_RESPONSE((byte) 5),
    DELETE_REQUEST((byte) 6),
    DELETE_RESPONSE((byte) 7),
    GET_REQUEST((byte) 8),
    GET_RESPONSE((byte) 9),
    PUT_REQUEST((byte) 10),
    PUT_RESPONSE((byte) 11),
    SUBSCRIBE_REQUEST((byte) 12),
    SUBSCRIBE_RESPONSE((byte) 13),
    UNSUBSCRIBE_REQUEST((byte) 14),
    UNSUBSCRIBE_RESPONSE((byte) 15),
    INCREMENTAL_UPDATE_REQUEST((byte) 16),
    INCREMENTAL_UPDATE_RESPONSE((byte) 17),
    DISPATCH_REQUEST((byte) 18),
    DISPATCH_RESPONSE((byte) 19),
    GET_CLIENT_LIST_REQUEST((byte) 20),
    GET_CLIENT_LIST_RESPONSE((byte) 21),
    SYSTEM_INFO_REQUEST((byte) 22),
    SYSTEM_INFO_RESPONSE((byte) 23);

    private byte value;

    public byte getValue() {
        return value;
    }

    private void setValue(byte value) {
        this.value = value;
    }

    MessageType(byte value) {
        this.setValue(value);
    }

    public static MessageType get(byte type) {
        for (MessageType elem : values()) {
            if (elem.getValue() == type) {
                return elem;
            }
        }
        throw new RuntimeException("Unsupported type: " + type);
    }
}
