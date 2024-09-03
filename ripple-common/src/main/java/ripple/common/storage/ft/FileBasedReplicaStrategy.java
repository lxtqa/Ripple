// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.ft;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class FileBasedReplicaStrategy implements ReplicaStrategy {
    private int count;
    private List<String> locations;

    @Override
    public int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public List<String> getLocations() {
        return locations;
    }

    @Override
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public FileBasedReplicaStrategy(List<String> locations) {
        try {
            this.setLocations(locations);
            this.setCount(locations.size());
            for (String location : locations) {
                Path path = Paths.get(location);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private int getQuorum() {
        return (int) (Math.floor((double) this.getCount() / 2) + 1);
    }

    private String doRead(String key) {
        try {
            List<String> values = new ArrayList<>();
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                if (!Files.exists(fileName)) {
                    return null;
                }
                byte[] value = Files.readAllBytes(fileName);
                values.add(new String(value, StandardCharsets.UTF_8));
            }
            Collections.sort(values);
            int i = 0;
            int count = 0;
            int maxCount = 1;
            int index = -1;
            for (i = 0; i < values.size(); i++) {
                if (i == 0) {
                    count = 1;
                } else if (values.get(i).equals(values.get(i - 1))) {
                    count++;
                    if (count >= maxCount) {
                        maxCount = count;
                        index = i;
                    }
                } else {
                    count = 1;
                }
            }
            if (maxCount >= this.getQuorum()) {
                return values.get(index);
            } else {
                return null;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public String read(String key) {
        return this.doRead(key);
    }

    private void doRepair(String key, String correctValue) {
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                byte[] value = Files.readAllBytes(fileName);
                String toCompare = new String(value, StandardCharsets.UTF_8);
                if (!toCompare.equals(correctValue)) {
                    Files.write(fileName, correctValue.getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String readAndRepair(String key) {
        String ret = this.doRead(key);
        if (ret != null) {
            this.doRepair(key, ret);
        }
        return ret;
    }

    @Override
    public void write(String key, String value) {
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                Files.write(fileName, value.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                if (Files.exists(fileName)) {
                    Files.deleteIfExists(fileName);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
