package ripple.test.tools;

import com.alibaba.nacos.api.config.ConfigService;
import ripple.client.RippleClient;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkloadGenerator {
    private WorkloadGenerator() {

    }

    public static void runRippleLoadTest(int qps, int duration, int payloadSize, int existingKeyCount, List<RippleClient> clientCluster) {
        try {
            Random random = new Random();
            // Prepare
            System.out.println("Preparing");
            int i = 0;
            for (i = 0; i < existingKeyCount; i++) {
                RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                // 1KB per entry
                client.put("testApp", "testKey-" + (i + 1), PayloadGenerator.generateKeyValuePair(16, 64));
            }
            System.out.println("Prepare done.");
            new Scanner(System.in).nextLine();

            int sleepTime = 1000 / qps;
            for (i = 0; i < duration * qps; i++) {
                RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                String value = System.currentTimeMillis() + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
                client.put("testApp", "test", value);
                Thread.sleep(sleepTime);
            }
            System.out.println("Done.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static class TestTask implements Callable<Void> {
        private ConfigService client;
        private String payload;

        public TestTask(ConfigService client, String payload) {
            this.client = client;
            this.payload = payload;
        }

        @Override
        public Void call() throws Exception {
            this.client.publishConfig("testApp", "test", this.payload);
            return null;
        }
    }

    public static void runNacosLoadTest(int qps, int duration, int payloadSize, int existingKeyCount, List<ConfigService> clientCluster) {
        try {
            Random random = new Random();
            // Prepare
            System.out.println("Preparing");
            int i = 0;
            for (i = 0; i < existingKeyCount; i++) {
                ConfigService client = clientCluster.get(random.nextInt(clientCluster.size()));
                // 1KB per entry
                client.publishConfig("testApp", "testKey-" + (i + 1), PayloadGenerator.generateKeyValuePair(16, 64));
            }

            ExecutorService pool = Executors.newFixedThreadPool(clientCluster.size());
            int sleepTime = 1000 / qps;
            for (i = 0; i < duration * qps; i++) {
                ConfigService client = clientCluster.get(random.nextInt(clientCluster.size()));
                String payload = System.currentTimeMillis() + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
                pool.submit(new TestTask(client, payload));
                Thread.sleep(sleepTime);
            }
            pool.shutdown();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
