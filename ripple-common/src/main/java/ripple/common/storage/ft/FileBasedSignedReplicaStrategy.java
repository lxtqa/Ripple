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
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class FileBasedSignedReplicaStrategy implements ReplicaStrategy, SignatureStrategy {
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

    public FileBasedSignedReplicaStrategy(List<String> locations) {
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

    @Override
    public String read(String key) {
        // Return first replica whose signature is correct
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                Path signatureFileName = Paths.get(location, SignatureHelper.getSignatureFileName(key));
                if (!Files.exists(fileName) || !Files.exists(signatureFileName)) {
                    continue;
                }
                byte[] value = Files.readAllBytes(fileName);
                byte[] digest = SignatureHelper.calculateSignature(value);
                byte[] digestInDisk = Files.readAllBytes(signatureFileName);
                if (!Arrays.equals(digest, digestInDisk)) {
                    continue;
                }
                return new String(value, StandardCharsets.UTF_8);
            }
            return null;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public String readAndRepair(String key) {
        byte[] correctValue = null;
        byte[] correctDigest = null;
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                Path signatureFileName = Paths.get(location, SignatureHelper.getSignatureFileName(key));
                if (!Files.exists(fileName) || !Files.exists(signatureFileName)) {
                    continue;
                }
                byte[] value = Files.readAllBytes(fileName);
                byte[] digest = SignatureHelper.calculateSignature(value);
                byte[] digestInDisk = Files.readAllBytes(signatureFileName);
                if (Arrays.equals(digest, digestInDisk)) {
                    correctValue = value;
                    correctDigest = digest;
                    break;
                }
            }
            if (correctValue == null && correctDigest == null) {
                // Not found
                return null;
            } else {
                for (String location : this.getLocations()) {
                    Path fileName = Paths.get(location, key);
                    Path signatureFileName = Paths.get(location, SignatureHelper.getSignatureFileName(key));
                    Files.write(fileName, correctValue);
                    Files.write(signatureFileName, correctDigest);
                }
                return new String(correctValue, StandardCharsets.UTF_8);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(String key, String value) {
        try {
            for (String location : this.getLocations()) {
                Path fileName = Paths.get(location, key);
                Path signatureFileName = Paths.get(location, SignatureHelper.getSignatureFileName(key));
                byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
                Files.write(fileName, valueBytes);
                byte[] digest = SignatureHelper.calculateSignature(valueBytes);
                Files.write(signatureFileName, digest);
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
                Path signatureFileName = Paths.get(location, SignatureHelper.getSignatureFileName(key));
                if (Files.exists(fileName)) {
                    Files.deleteIfExists(fileName);
                }
                if (Files.exists(signatureFileName)) {
                    Files.deleteIfExists(signatureFileName);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
