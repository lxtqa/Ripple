package ripple.common;

import ripple.common.entity.ClientMetadata;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.setMap(new HashMap<>());
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
