package ripple.test.remote;

import io.netty.channel.Channel;
import ripple.client.RippleClient;
import ripple.client.core.tcp.handler.DispatchRequestHandler;
import ripple.client.core.tcp.handler.SyncRequestHandler;
import ripple.client.helper.Api;
import ripple.common.entity.*;
import ripple.test.tools.WorkloadGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Zhen Tang
 */
public class TestRipple {
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";
    private static final List<NodeMetadata> CLUSTER_VM_LOCAL = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.21", 3000)
            , new NodeMetadata(2, "192.168.2.22", 3000)
            , new NodeMetadata(3, "192.168.2.23", 3000)));

    private static final List<NodeMetadata> CLUSTER_LAB = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "133.133.135.154", 3000)
            , new NodeMetadata(2, "133.133.135.155", 3000)
            , new NodeMetadata(3, "133.133.135.156", 3000)
            , new NodeMetadata(4, "133.133.135.157", 3000)));

    private static final List<NodeMetadata> CLUSTER_VM_LAB_4_NODES = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.11", 3000)
            , new NodeMetadata(2, "192.168.2.12", 3000)
            , new NodeMetadata(3, "192.168.2.13", 3000)
            , new NodeMetadata(4, "192.168.2.14", 3000)));

    private static final List<NodeMetadata> CLUSTER_VM_LAB_8_NODES = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.11", 3000)
            , new NodeMetadata(2, "192.168.2.12", 3000)
            , new NodeMetadata(3, "192.168.2.13", 3000)
            , new NodeMetadata(4, "192.168.2.14", 3000)
            , new NodeMetadata(5, "192.168.2.15", 3000)
            , new NodeMetadata(6, "192.168.2.16", 3000)
            , new NodeMetadata(7, "192.168.2.17", 3000)
            , new NodeMetadata(8, "192.168.2.18", 3000)));

    private static final List<NodeMetadata> CLUSTER_VM_LAB_16_NODES = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.11", 3000)
            , new NodeMetadata(2, "192.168.2.12", 3000)
            , new NodeMetadata(3, "192.168.2.13", 3000)
            , new NodeMetadata(4, "192.168.2.14", 3000)
            , new NodeMetadata(5, "192.168.2.15", 3000)
            , new NodeMetadata(6, "192.168.2.16", 3000)
            , new NodeMetadata(7, "192.168.2.17", 3000)
            , new NodeMetadata(8, "192.168.2.18", 3000)
            , new NodeMetadata(9, "192.168.2.19", 3000)
            , new NodeMetadata(10, "192.168.2.20", 3000)
            , new NodeMetadata(11, "192.168.2.21", 3000)
            , new NodeMetadata(12, "192.168.2.22", 3000)
            , new NodeMetadata(13, "192.168.2.23", 3000)
            , new NodeMetadata(14, "192.168.2.24", 3000)
            , new NodeMetadata(15, "192.168.2.25", 3000)
            , new NodeMetadata(16, "192.168.2.26", 3000)));

    public static void testSubscribe(int publishCount, List<RippleClient> rippleClients) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int i = 0;
        for (i = 0; i < rippleClients.size(); i++) {
            RippleClient rippleClient = rippleClients.get(i);
            long startTime = System.nanoTime();
            // rippleClient.subscribe("testApp", "test-" + (i % publishCount));
            rippleClient.findOrConnectToServer("testApp", "test");
            rippleClient.subscribe("testApp", "test");
            long endTime = System.nanoTime();
            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                    + "] Subscribed: " + (endTime - startTime + 0.00) / 1000 / 1000 + "ms. " + (i + 1) + " / " + rippleClients.size());
        }
    }

    public static void testUnsubscribe(int publishCount, List<RippleClient> rippleClients) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int i = 0;
        for (i = 0; i < rippleClients.size(); i++) {
            RippleClient rippleClient = rippleClients.get(i);
            long startTime = System.nanoTime();
            // rippleClient.unsubscribe("testApp", "test-" + (i % publishCount));
            rippleClient.unsubscribe("testApp", "test");
            long endTime = System.nanoTime();
            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                    + "] Unsubscribed: " + (endTime - startTime + 0.00) / 1000 / 1000 + "ms");
        }
    }

    public static void testPutAndGet(int publishCount, List<RippleClient> clients) {
        int i = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Publishing.");
        for (i = 0; i < publishCount; i++) {
            // clients.get(i).put("testApp", "test-" + i, WorkloadGenerator.generateKeyValuePair(16, 64));
            clients.get(i).put("testApp", "test", WorkloadGenerator.generateKeyValuePair(16, 64));
        }
    }

    public static UUID initIncrementalUpdate(int publishCount, List<RippleClient> clients) {
        int i = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Publishing.");
        for (i = 0; i < publishCount; i++) {
            // 1KB value
            String value = WorkloadGenerator.generateKeyValuePair(16, 64);
            // clients.get(i).put("testApp", "test-" + i, WorkloadGenerator.generateKeyValuePair(16, 64));
            clients.get(i).put("testApp", "test", value);
        }
        Item item = clients.get(i).get("testApp", "test");
        List<AbstractMessage> list = clients.get(i).getStorage().getMessageService().findMessages(item.getApplicationName(), item.getKey());
        return list.get(0).getUuid();
    }

    public static void testIncrementalUpdate(UUID baseUuid, int publishCount, List<RippleClient> clients) {
        int i = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Publishing.");
        for (i = 0; i < publishCount; i++) {
            // 128 bytes
            String value = WorkloadGenerator.generateKeyValuePair(2, 64);
            clients.get(i).incrementalUpdate("testApp", "test", baseUuid
                    , Constants.ATOMIC_OPERATION_ADD_ENTRY, value);
        }
    }

    public static void main(String[] args) {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
            int totalClientCount = 2500;
            int publishCount = 40;

            int i = 0;
            List<RippleClient> rippleClients = new ArrayList<>();

            ExecutorService pool = Executors.newFixedThreadPool(totalClientCount);
            for (i = 0; i < totalClientCount; i++) {
                Files.createDirectories(Paths.get(DATABASE_PATH));
                String storageLocation = DATABASE_PATH + "\\" + UUID.randomUUID().toString() + ".db";
                RippleClient rippleClient = new RippleClient(CLUSTER_VM_LAB_16_NODES, storageLocation);
                pool.submit(new StartTask(rippleClient, i + 1));
                rippleClients.add(rippleClient);
            }
            pool.shutdown();
            System.out.println("Done.");

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            testSubscribe(publishCount, rippleClients);
            System.out.println("Subscribe done.");
            scanner.nextLine();

            UUID baseUuid = initIncrementalUpdate(publishCount, rippleClients);
            scanner.nextLine();
            int loops = 12;
            for (i = 0; i < loops; i++) {
                long startTime = System.nanoTime();
                DispatchRequestHandler.StartTime = startTime;
                SyncRequestHandler.StartTime = startTime;
                // testPutAndGet(publishCount, rippleClients);
                testIncrementalUpdate(baseUuid, publishCount, rippleClients);
                System.out.println("Loop " + (i + 1) + " done.");
                scanner.nextLine();
            }

            testUnsubscribe(publishCount, rippleClients);
            System.out.println("Unsubscribe done.");
            scanner.nextLine();

            System.out.println("Stopping clients.");

            pool = Executors.newFixedThreadPool(totalClientCount);
            for (i = 0; i < totalClientCount; i++) {
                RippleClient rippleClient = rippleClients.get(i);
                pool.submit(new StopTask(rippleClient, i + 1));
            }
            pool.shutdown();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static class StartTask implements Callable<Void> {
        private RippleClient client;
        private int id;

        public StartTask(RippleClient client, int id) {
            this.client = client;
            this.id = id;
        }

        @Override
        public Void call() throws Exception {
            this.client.start();
            System.out.println("Started: " + this.id);
            return null;
        }
    }

    static class StopTask implements Callable<Void> {
        private RippleClient client;
        private int id;

        public StopTask(RippleClient client, int id) {
            this.client = client;
            this.id = id;
        }

        @Override
        public Void call() throws Exception {
            this.client.stop();
            System.out.println("Stopped: " + this.id);
            return null;
        }
    }
}
