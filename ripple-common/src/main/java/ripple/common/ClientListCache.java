// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common;

import ripple.common.entity.ClientMetadata;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class ClientListCache {
    private Map<String, List<ClientMetadata>> map;

    private Map<String, List<ClientMetadata>> getMap() {
        return map;
    }

    private void setMap(Map<String, List<ClientMetadata>> map) {
        this.map = map;
    }

    public ClientListCache() {
        this.setMap(new ConcurrentHashMap<>());
    }

    public List<ClientMetadata> get(String signature) {
        return this.getMap().get(signature);
    }

    public void put(String signature, List<ClientMetadata> list) {
        this.getMap().put(signature, list);
    }

    public String calculateSignature(List<ClientMetadata> clientList) {
        try {
            List<ClientMetadata> list = new ArrayList<>(clientList);
            Collections.sort(list);
            String source = "";
            for (ClientMetadata clientMetadata : list) {
                source += clientMetadata.toString();
            }
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] hash = messageDigest.digest(source.getBytes(StandardCharsets.UTF_8));
            return ClientListCache.byteArrayToHex(hash);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static String byteArrayToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
