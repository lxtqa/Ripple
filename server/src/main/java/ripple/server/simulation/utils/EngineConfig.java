package ripple.server.simulation.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"config/engine.properties"})
public class EngineConfig {
    @Value("${engine.ip}")
    private String engineIp;

    public String getEngineIp() {
        return engineIp;
    }

    public void setEngineIp(String engineIp) {
        this.engineIp = engineIp;
    }
}
