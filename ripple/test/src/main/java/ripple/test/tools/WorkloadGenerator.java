package ripple.test.tools;

import com.alibaba.nacos.api.config.ConfigService;
import ripple.client.RippleClient;

import java.util.List;
import java.util.Random;

public class WorkloadGenerator {
    private WorkloadGenerator() {

    }

    public static void runRippleLoadTest(int qps, int duration, int payloadSize, int existingKeyCount, List<RippleClient> clientCluster) {
        try {
            Random random = new Random();
            // Prepare
            int i = 0;
            for (i = 0; i < existingKeyCount; i++) {
                RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                // 1KB per entry
                client.put("testApp", "testKey-" + (i + 1), PayloadGenerator.generateKeyValuePair(16, 64));
            }

            int sleepTime = 1000 / qps;
            for (i = 0; i < duration * qps; i++) {
                RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                String value = System.currentTimeMillis() + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
                client.put("testApp", "test", value);
                Thread.sleep(sleepTime);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void runNacosLoadTest(int qps, int duration, int payloadSize, int existingKeyCount, List<ConfigService> clientCluster) {
        try {
            Random random = new Random();
            // Prepare
            int i = 0;
            for (i = 0; i < existingKeyCount; i++) {
                ConfigService client = clientCluster.get(random.nextInt(clientCluster.size()));
                // 1KB per entry
                client.publishConfig("testApp", "testKey-" + (i + 1), PayloadGenerator.generateKeyValuePair(16, 64));
            }

            int sleepTime = 1000 / qps;
            for (i = 0; i < duration * qps; i++) {
                ConfigService client = clientCluster.get(random.nextInt(clientCluster.size()));
                String value = System.currentTimeMillis() + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
                client.publishConfig("testApp", "test", value);
                Thread.sleep(sleepTime);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
