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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Zhen Tang
 */
public class FileBasedSignatureStrategy implements SignatureStrategy {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public FileBasedSignatureStrategy(String location) {
        try {
            this.setLocation(location);
            Path path = Paths.get(location);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String read(String key) {
        try {
            Path fileName = Paths.get(this.getLocation(), key);
            Path signatureFileName = Paths.get(this.getLocation(), SignatureHelper.getSignatureFileName(key));
            if (!Files.exists(fileName) || !Files.exists(signatureFileName)) {
                return null;
            }
            byte[] value = Files.readAllBytes(fileName);
            byte[] digest = SignatureHelper.calculateSignature(value);
            byte[] digestInDisk = Files.readAllBytes(signatureFileName);
            if (!Arrays.equals(digest, digestInDisk)) {
                return null;
            }
            return new String(value, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(String key, String value) {
        try {
            Path fileName = Paths.get(this.getLocation(), key);
            Path signatureFileName = Paths.get(this.getLocation(), SignatureHelper.getSignatureFileName(key));
            byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
            Files.write(fileName, valueBytes);
            byte[] digest = SignatureHelper.calculateSignature(valueBytes);
            Files.write(signatureFileName, digest);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        try {
            Path fileName = Paths.get(this.getLocation(), key);
            Path signatureFileName = Paths.get(this.getLocation(), SignatureHelper.getSignatureFileName(key));
            if (Files.exists(fileName)) {
                Files.deleteIfExists(fileName);
            }
            if (Files.exists(signatureFileName)) {
                Files.deleteIfExists(signatureFileName);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
