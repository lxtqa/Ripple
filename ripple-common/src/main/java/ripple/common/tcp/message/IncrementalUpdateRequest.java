// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateRequest extends Message {
    private String applicationName;
    private String key;
    private UUID baseMessageUuid;
    private String atomicOperation;
    private String value;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UUID getBaseMessageUuid() {
        return baseMessageUuid;
    }

    public void setBaseMessageUuid(UUID baseMessageUuid) {
        this.baseMessageUuid = baseMessageUuid;
    }

    public String getAtomicOperation() {
        return atomicOperation;
    }

    public void setAtomicOperation(String atomicOperation) {
        this.atomicOperation = atomicOperation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IncrementalUpdateRequest() {
        this.setType(MessageType.INCREMENTAL_UPDATE_REQUEST);
    }
}
