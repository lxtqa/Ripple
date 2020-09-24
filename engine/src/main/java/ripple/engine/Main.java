package ripple.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author fuxiao.tz
 */
@SpringBootApplication
public class Main {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static void setApplicationContext(ApplicationContext applicationContext) {
        Main.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        Main.setApplicationContext(SpringApplication.run(Main.class, args));
    }
}
