package ripple.test.remote;

import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zhen Tang
 */
public class TestRipple {
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";
    private static final List<NodeMetadata> VM_CLUSTER = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.21", 3000)
            , new NodeMetadata(2, "192.168.2.22", 3000)
            , new NodeMetadata(3, "192.168.2.23", 3000)));

    public static void testSubscribe(List<RippleClient> rippleClients) throws IOException {
        long startTime = System.nanoTime();
        int i;
        for (RippleClient rippleClient : rippleClients) {
            rippleClient.subscribe("testApp", "test");
        }
        long endTime = System.nanoTime();
        long time = endTime - startTime;
        System.out.println("Subscribe: " + (double) time / rippleClients.size() + " ns.");
    }

    public static void testUnsubscribe(List<RippleClient> rippleClients) throws IOException {
        long startTime = System.nanoTime();
        int i;
        for (RippleClient rippleClient : rippleClients) {
            rippleClient.unsubscribe("testApp", "test");
        }
        long endTime = System.nanoTime();
        long time = endTime - startTime;
        System.out.println("Unubscribe: " + (double) time / rippleClients.size() + " ns.");
    }

    public static void testPutAndGet(RippleClient client) {
        client.put("testApp", "test", UUID.randomUUID().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Published.");
    }

    public static void main(String[] args) {
        try {
            int count = 20;
            int i = 0;
            List<RippleClient> rippleClients = new ArrayList<>();
            for (i = 0; i < count; i++) {
                Files.createDirectories(Paths.get(DATABASE_PATH));
                String storageLocation = DATABASE_PATH + "\\" + UUID.randomUUID().toString() + ".db";
                RippleClient rippleClient = new RippleClient(VM_CLUSTER, storageLocation);
                rippleClient.start();
                rippleClients.add(rippleClient);
            }
            testSubscribe(rippleClients);
            testPutAndGet(rippleClients.get(0));
            System.in.read();
            testUnsubscribe(rippleClients);
            for (RippleClient rippleClient : rippleClients) {
                rippleClient.stop();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
