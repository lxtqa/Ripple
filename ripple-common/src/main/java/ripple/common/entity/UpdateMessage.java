// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.entity;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class UpdateMessage extends AbstractMessage {
    private String value;

    public UpdateMessage(UUID uuid, String applicationName, String key, String value
            , Date lastUpdate, int lastUpdateServerId) {
        super(uuid, Constants.MESSAGE_TYPE_UPDATE, applicationName, key, lastUpdate, lastUpdateServerId);
        this.setValue(value);
    }

    public UpdateMessage(String applicationName, String key, String value
            , Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, value, lastUpdate, lastUpdateServerId);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UpdateMessage() {

    }
}
