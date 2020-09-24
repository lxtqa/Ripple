package ripple.agent.helper;

import ripple.agent.core.Config;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@Component
public class RegistryHelper {
    private Config config;

    private Config getConfig() {
        return config;
    }

    @Autowired
    private void setConfig(Config config) {
        this.config = config;
    }

    public void registerAgent(UUID uuid, String address, int port) {
        String url = "http://" + this.getConfig().getEngineAddrss() + ":" + this.getConfig().getEnginePort() + "/Api/Registry/Agent";
        Map<String, String> headers = new HashMap<>(3);
        headers.put("x-ripple-engine-uuid", uuid.toString());
        headers.put("x-ripple-engine-address", address);
        headers.put("x-ripple-engine-port", Integer.toString(port));
        HttpHelper.post(url, headers, "", ContentType.APPLICATION_JSON);
    }
}
