package ripple.test.tools;

import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;

import java.util.List;
import java.util.Random;

public class WorkloadGenerator {
    private WorkloadGenerator() {

    }

    public static void runLoadTest(int qps, int duration, int payloadSize, int existingKeyCount
            , List<NodeMetadata> serverCluster, List<RippleClient> clientCluster) {
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
}
