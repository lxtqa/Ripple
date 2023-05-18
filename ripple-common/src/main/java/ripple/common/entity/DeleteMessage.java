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
public class DeleteMessage extends AbstractMessage {

    public DeleteMessage(UUID uuid, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        super(uuid, Constants.MESSAGE_TYPE_DELETE, applicationName, key, lastUpdate, lastUpdateServerId);
    }

    public DeleteMessage(String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, lastUpdate, lastUpdateServerId);
    }

    public DeleteMessage() {

    }

}
