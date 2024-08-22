// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.
package ripple.common.storage.pm;

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public class ItemEntry {
    private String applicationName;
    private String key;

    public ItemEntry() {

    }

    public ItemEntry(String applicationName, String key) {
        this.setApplicationName(applicationName);
        this.setKey(key);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntry itemEntry = (ItemEntry) o;
        return Objects.equals(applicationName, itemEntry.applicationName) && Objects.equals(key, itemEntry.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, key);
    }
}
