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

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public class Item {
    private String applicationName;
    private String key;

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
        Item item = (Item) o;
        return Objects.equals(applicationName, item.applicationName) && Objects.equals(key, item.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, key);
    }

    @Override
    public String toString() {
        return "Item{" +
                "applicationName='" + applicationName + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public Item(String applicationName, String key) {
        this.setApplicationName(applicationName);
        this.setKey(key);
    }

    public Item() {

    }
}