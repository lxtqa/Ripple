// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core;

import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.Hashing;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class HashingBasedSelector implements NodeSelector {
    private Hashing hashing;

    public Hashing getHashing() {
        return hashing;
    }

    private void setHashing(Hashing hashing) {
        this.hashing = hashing;
    }

    public HashingBasedSelector(Hashing hashing) {
        this.setHashing(hashing);
    }

    //  Randomly select
    @Override
    public NodeMetadata selectNodeToConnect(String applicationName, String key, List<NodeMetadata> nodeList) {
        List<NodeMetadata> candidates = this.getHashing().hashing(applicationName, key, nodeList);
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }
}
