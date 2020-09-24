package ripple.agent;

import ripple.agent.helper.RegistryHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@SpringBootApplication
@PropertySource(value = {"config/config.properties"})
public class Main {
    private static ApplicationContext applicationContext;
    public static final UUID AGENT_UUID = UUID.randomUUID();

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static void setApplicationContext(ApplicationContext applicationContext) {
        Main.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        try {
            Main.setApplicationContext(SpringApplication.run(Main.class, args));
            RegistryHelper registryHelper = Main.getApplicationContext().getBean(RegistryHelper.class);
            registryHelper.registerAgent(AGENT_UUID, InetAddress.getLocalHost().getHostAddress(), 8001);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
