// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.crdt;

import ripple.common.entity.Item;

/**
 * A simple grow-only conflict resolver.
 * For concurrent updates which can not be ordered, it calculates the union
 *
 * @author Zhen Tang
 */
public class GrowOnlyConflictResolver implements StateBasedConflictResolver {
    private long maxTimeDifference;

    public GrowOnlyConflictResolver(long maxTimeDifference) {
        this.setMaxTimeDifference(maxTimeDifference);
    }

    public long getMaxTimeDifference() {
        return maxTimeDifference;
    }

    private void setMaxTimeDifference(long maxTimeDifference) {
        this.maxTimeDifference = maxTimeDifference;
    }

    @Override
    public void merge(Item current, Item target) {
        // TODO
//        if (!(current.getValue() instanceof String)) {
//            // Only merge instances now
//            return;
//        }
//        long timeDifference = target.getLastUpdate().getTime() - current.getLastUpdate().getTime();
//        if (timeDifference < 0 && (-timeDifference) > this.getMaxTimeDifference()) {
//            // Received obsoleted data, discard
//            return;
//        }
//        if (timeDifference > 0 && timeDifference > this.getMaxTimeDifference()) {
//            // Accept
//            current.setValue(target.getValue());
//            current.getLastUpdate().setTime(target.getLastUpdate().getTime());
//        } else {
//            // Resolve conflict. Merge two instance lists
//            String currentValue = current.getValue();
//            String toMergeValue = target.getValue();
//            String mergedValue = this.doMerge(currentValue, toMergeValue);
//            current.setValue(mergedValue);
//            if (timeDifference > 0) {
//                current.getLastUpdate().setTime(target.getLastUpdate().getTime());
//            }
//        }
    }

    private String doMerge(String currentValue, String toMergeValue) {
        // Merge values here
        return toMergeValue;
    }
}

