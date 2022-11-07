package ripple.test.remote;

import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class TestSubscribe {
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";
    private static final List<NodeMetadata> VM_CLUSTER = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.21", 3000)
            , new NodeMetadata(2, "192.168.2.21", 3000)
            , new NodeMetadata(3, "192.168.2.21", 3000)));

    public static void testSubscribe(RippleClient rippleClient) throws IOException {
        long startTime = System.nanoTime();
        rippleClient.subscribe("testApp", "test");
        rippleClient.unsubscribe("testApp", "test");
        long endTime = System.nanoTime();
        long time = endTime - startTime;
        System.out.println("Subscribe: " + time + " ns.");
    }

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DATABASE_PATH));
            String storageLocation = DATABASE_PATH + "\\" + UUID.randomUUID().toString();
            RippleClient rippleClient = new RippleClient(VM_CLUSTER, storageLocation);
            rippleClient.start();
            testSubscribe(rippleClient);
            System.in.read();
            rippleClient.stop();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
