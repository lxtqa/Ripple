package ripple.test.padres;

import ca.utoronto.msrg.padres.common.message.parser.MessageFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class WorkloadGenerator {
    private WorkloadGenerator() {

    }

    public static void runPadresLoadTest(int qps, int duration, int payloadSize, int existingKeyCount, List<TestPadresClient> clientCluster) {
        try {
            Random random = new Random();
            // Prepare
            int i = 0;
            for (i = 0; i < existingKeyCount; i++) {
                TestPadresClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                UUID uuid = UUID.randomUUID();
                client.advertise(MessageFactory.createAdvertisementFromString("[class,eq,'temp" + uuid.toString() + "'],[content,str-contains,' '],[value,<,0]"));
                // 1KB Per entry
                client.publish(MessageFactory.createPublicationFromString(
                        "[class,'temp" + uuid.toString() + "'],[content,'" + PayloadGenerator.generateKeyValuePair(16, 64) + "'],[value,-" + (i + 1) + "]"));
            }

            int sleepTime = 1000 / qps;
            for (i = 0; i < duration * qps; i++) {
                TestPadresClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                String value = System.currentTimeMillis() + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
                client.advertise(MessageFactory.createAdvertisementFromString("[class,eq,'temp'],[content,str-contains,' '],[value,<,0]"));
                client.publish(MessageFactory.createPublicationFromString("[class,'temp'],[content,'" + value + "'],[value,-" + (i + 1) + "]"));
                Thread.sleep(sleepTime);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
