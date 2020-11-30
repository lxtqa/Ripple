package ripple.server.simulation.core.emulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author fuxiao.tz
 */
@Component
@PropertySource(value = {"config/config.properties"})
public class EmulatorConfig {
    @Value("${emulator.databaseLatency}")
    private int databaseLatency;
    @Value("${emulator.networkLatency}")
    private int networkLatency;

    public int getDatabaseLatency() {
        return databaseLatency;
    }

    public void setDatabaseLatency(int databaseLatency) {
        this.databaseLatency = databaseLatency;
    }

    public int getNetworkLatency() {
        return networkLatency;
    }

    public void setNetworkLatency(int networkLatency) {
        this.networkLatency = networkLatency;
    }
}
