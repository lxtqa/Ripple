// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.dir;

import ripple.common.entity.Ack;
import ripple.common.storage.AckService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class DirectoryBasedAckService implements AckService {
    @Override
    public boolean initAck(UUID messageUuid, List<Integer> nodeList) {
        // TODO
        return false;
    }

    @Override
    public Ack getAck(UUID messageUuid) {
        // TODO
        return null;
    }

    @Override
    public List<Ack> getAllAcks() {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public boolean recordAck(UUID messageUuid, int serverId) {
        // TODO
        return false;
    }
}
