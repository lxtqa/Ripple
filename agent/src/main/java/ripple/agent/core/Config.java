package ripple.agent.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author fuxiao.tz
 */
@Component
@PropertySource(value = {"config/config.properties"})
public class Config {
    @Value("${engine.address}")
    private String engineAddrss;
    @Value("${engine.port}")
    private int enginePort;

    public String getEngineAddrss() {
        return engineAddrss;
    }

    public void setEngineAddrss(String engineAddrss) {
        this.engineAddrss = engineAddrss;
    }

    public int getEnginePort() {
        return enginePort;
    }

    public void setEnginePort(int enginePort) {
        this.enginePort = enginePort;
    }
}
