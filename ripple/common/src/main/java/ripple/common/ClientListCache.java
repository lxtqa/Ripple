package ripple.common;

import ripple.common.entity.ClientMetadata;

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
}
