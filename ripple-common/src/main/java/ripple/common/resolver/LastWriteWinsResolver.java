// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.resolver;

import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.Item;
import ripple.common.entity.UpdateMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhen Tang
 */
public class LastWriteWinsResolver implements MessageBasedResolver {
    // TODO: (trick) Only merge by appending and replacing
    // TODO: (trick) Only handle incremental update on an existing update message
    private String mergeValue(IncrementalUpdateMessage current, AbstractMessage base) {
        if (base instanceof UpdateMessage) {
            switch (current.getAtomicOperation()) {
                case Constants.ATOMIC_OPERATION_ADD_ENTRY:
                    return ((UpdateMessage) base).getValue() + current.getValue();
                case Constants.ATOMIC_OPERATION_REMOVE_ENTRY:
                    return ((UpdateMessage) base).getValue().replace(current.getValue(), "");
            }
        }
        return null;
    }

    @Override
    public void merge(Item current, List<AbstractMessage> history) {
        List<AbstractMessage> messages = new ArrayList<>(history);
        messages.sort((one, other) -> (int) (other.getLastUpdate().getTime() - one.getLastUpdate().getTime()));
        AbstractMessage wins = messages.get(0);
        if (wins instanceof UpdateMessage) {
            current.setValue(((UpdateMessage) wins).getValue());
        } else if (wins instanceof DeleteMessage) {
            current.setValue(null);
        } else if (wins instanceof IncrementalUpdateMessage) {
            Optional<AbstractMessage> baseMessage = messages.stream()
                    .filter(m -> m.getUuid().equals(((IncrementalUpdateMessage) wins).getBaseMessageUuid())).findAny();
            if (baseMessage.isPresent()) {
                current.setValue(this.mergeValue((IncrementalUpdateMessage) wins, baseMessage.get()));
            }
        }
    }
}
