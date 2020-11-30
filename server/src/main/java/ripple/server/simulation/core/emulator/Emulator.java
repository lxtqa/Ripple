package ripple.server.simulation.core.emulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fuxiao.tz
 */
@Component
public class Emulator {
    private static Logger LOGGER = LoggerFactory.getLogger(Emulator.class);
    private EmulatorConfig emulatorConfig;

    public EmulatorConfig getEmulatorConfig() {
        return emulatorConfig;
    }

    @Autowired
    public void setEmulatorConfig(EmulatorConfig emulatorConfig) {
        this.emulatorConfig = emulatorConfig;
    }

    public void databaseOperation() {
        try {
            LOGGER.trace("database operation: sleep for {} ms.", this.getEmulatorConfig().getDatabaseLatency());
            Thread.sleep(this.getEmulatorConfig().getDatabaseLatency());
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void networkOperation() {
        try {
            LOGGER.trace("network operation: sleep for {} ms.", this.getEmulatorConfig().getNetworkLatency());
            Thread.sleep(this.getEmulatorConfig().getNetworkLatency());
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
