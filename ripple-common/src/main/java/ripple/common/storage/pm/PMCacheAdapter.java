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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.storage.StorageHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Zhen Tang
 */
public class PMCacheAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PMCacheAdapter.class);
    private static boolean BypassPMCache;

    static {
        try {
            System.loadLibrary("PMCacheAdapter");
            PMCacheAdapter.BypassPMCache = false;
        } catch (UnsatisfiedLinkError exception) {
            PMCacheAdapter.BypassPMCache = true;
            LOGGER.info("Cannot find PMCache library, using filesystem based key-value storage.");
        }
    }

    private long handle;
    private String cacheLocation;
    private String storageLocation;

    public long getHandle() {
        return handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }

    public String getCacheLocation() {
        return cacheLocation;
    }

    public void setCacheLocation(String cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    private native byte[] cacheGet(long handle, byte[] key);

    private native boolean cachePut(long handle, byte[] key, byte[] value);

    private native boolean cacheDelete(long handle, byte[] key);

    private native long openCache(String location);

    private native void closeCache(long handle);

    public void open(String cacheLocation, String storageLocation) {
        try {
            this.setCacheLocation(cacheLocation);
            this.setStorageLocation(storageLocation);
            Files.createDirectories(Paths.get(storageLocation));
            if (!BypassPMCache) {
                this.setHandle(this.openCache(cacheLocation));
            } else {
                LOGGER.info("Underlying open called.");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        if (!BypassPMCache) {
            this.closeCache(this.getHandle());
        } else {
            LOGGER.info("Underlying close called.");
        }
    }

    public byte[] get(byte[] key) {
        if (!BypassPMCache) {
            byte[] ret = this.cacheGet(this.getHandle(), key);
            if (ret == null) {
                return this.underlyingGet(key);
            }
            return ret;
        } else {
            return this.underlyingGet(key);
        }
    }

    public boolean put(byte[] key, byte[] value) {
        if (!BypassPMCache) {
            return this.cachePut(this.getHandle(), key, value);
        } else {
            return this.underlyingPut(key, value);
        }
    }

    public boolean delete(byte[] key) {
        if (!BypassPMCache) {
            return this.cacheDelete(this.getHandle(), key);
        } else {
            return this.underlyingDelete(key);
        }
    }

    private byte[] underlyingGet(byte[] key) {
        try {
            String keyString = new String(key, StandardCharsets.UTF_8);
            LOGGER.info("Underlying Get called. Key = {}", keyString);
            Path fileName = Paths.get(this.getStorageLocation(), StorageHelper.encodeString(keyString));
            if (Files.exists(fileName)) {
                return Files.readAllBytes(fileName);
            }
            return null;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private boolean underlyingPut(byte[] key, byte[] value) {
        try {
            String keyString = new String(key, StandardCharsets.UTF_8);
            String valueString = new String(value, StandardCharsets.UTF_8);
            Path fileName = Paths.get(this.getStorageLocation(), StorageHelper.encodeString(keyString));
            Files.write(fileName, value);
            LOGGER.info("Underlying Put called. Key = {}, Value = {}", keyString, valueString);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private boolean underlyingDelete(byte[] key) {
        try {
            String keyString = new String(key, StandardCharsets.UTF_8);
            LOGGER.info("Underlying Delete called. Key = {}", keyString);
            Path fileName = Paths.get(this.getStorageLocation(), StorageHelper.encodeString(keyString));
            if (Files.exists(fileName)) {
                return Files.deleteIfExists(fileName);
            }
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
