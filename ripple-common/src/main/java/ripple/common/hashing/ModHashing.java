// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.hashing;

import ripple.common.entity.NodeMetadata;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

public class ModHashing implements Hashing {
    private int count;
    private int divisor;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    public ModHashing() {
        this(3, 200);
    }

    public ModHashing(int count, int divisor) {
        this.setCount(count);
        this.setDivisor(divisor);
    }

    private String generateHashKey(String applicationName, String key) {
        return applicationName + key;
    }

    @Override
    public List<NodeMetadata> hashing(String applicationName, String key, List<NodeMetadata> nodeList) {
        String hashKey = this.generateHashKey(applicationName, key);
        byte[] bytes = hashKey.getBytes(StandardCharsets.UTF_8);
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        long result = crc32.getValue();
        long firstIndex = result % this.getDivisor();

        if (nodeList.size() <= this.getCount()) {
            return new ArrayList<>(nodeList);
        } else {
            List<NodeMetadata> ret = new ArrayList<>();
            int i;
            for (i = 0; i < this.getCount(); i++) {
                ret.add(nodeList.get((int) ((firstIndex + i) % nodeList.size())));
            }
            return ret;
        }
    }
}
