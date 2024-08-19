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

import java.nio.charset.StandardCharsets;

/**
 * @author Zhen Tang
 */
public class PMCacheAdapter {
    private static boolean BypassPMCache;

    static {
        try {
            System.loadLibrary("PMCacheAdapter");
            PMCacheAdapter.BypassPMCache = false;
        } catch (UnsatisfiedLinkError exception) {
            PMCacheAdapter.BypassPMCache = true;
            System.out.println("Cannot find PMCache library, using filesystem based key-value storage.");
        }
    }

    private long handle;

    public long getHandle() {
        return handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }

    private native byte[] cacheGet(long handle, byte[] key);

    private native boolean cachePut(long handle, byte[] key, byte[] value);

    private native boolean cacheDelete(long handle, byte[] key);

    private native long openCache(String location);

    private native void closeCache(long handle);

    public void open(String location) {
        if (!BypassPMCache) {
            this.setHandle(this.openCache(location));
        } else {
            // TODO
            System.out.println("Underlying open called.");
        }
    }

    public void close() {
        if (!BypassPMCache) {
            this.closeCache(this.getHandle());
        } else {
            // TODO
            System.out.println("Underlying close called.");
        }
    }

    public byte[] get(byte[] key) {
        if (!BypassPMCache) {
            return this.cacheGet(this.getHandle(), key);
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
        // TODO
        System.out.println("Underlying Get called. Key = " + new String(key, StandardCharsets.UTF_8));
        return null;
    }

    private boolean underlyingPut(byte[] key, byte[] value) {
        // TODO
        System.out.println("Underlying Put called. Key = " + new String(key, StandardCharsets.UTF_8)
                + ", Value = " + new String(value, StandardCharsets.UTF_8));
        return false;
    }

    private boolean underlyingDelete(byte[] key) {
        // TODO
        System.out.println("Underlying Delete called. Key = " + new String(key, StandardCharsets.UTF_8));
        return false;
    }

    public static void main(String[] args) {
        PMCacheAdapter adapter = new PMCacheAdapter();
        adapter.open("/mnt/pmem0/pool");
        byte[] getResult = adapter.get("testKey".getBytes(StandardCharsets.UTF_8));
        System.out.println(getResult == null ? "Get: null" : "Get: " + new String(getResult, StandardCharsets.UTF_8));
        adapter.put("testKey".getBytes(StandardCharsets.UTF_8), "testValue".getBytes(StandardCharsets.UTF_8));
        adapter.delete("testKey".getBytes(StandardCharsets.UTF_8));
        adapter.close();
    }
}
