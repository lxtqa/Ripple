// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.test.remote;

import ripple.client.RippleClient;
import ripple.client.core.HashingBasedSelector;
import ripple.common.entity.Constants;
import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.ModHashing;
import ripple.test.tools.PayloadGenerator;
import ripple.test.tools.WorkloadGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zhen Tang
 */
public class TestRipple {
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";
    private static final List<NodeMetadata> CLUSTER_VM_LAB = new ArrayList<>(Arrays.asList(
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
            , new NodeMetadata(16, "192.168.2.26", 3000)
            , new NodeMetadata(17, "192.168.2.27", 3000)
            , new NodeMetadata(18, "192.168.2.28", 3000)
            , new NodeMetadata(19, "192.168.2.29", 3000)
            , new NodeMetadata(20, "192.168.2.30", 3000)));

    public static void testSubscribe(List<RippleClient> rippleClients) throws IOException {
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

    public static void testUnsubscribe(List<RippleClient> rippleClients) throws IOException {
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
            clients.get(i).put("testApp", "test", PayloadGenerator.generateKeyValuePair(16, 64));
        }
    }

    public static void initIncrementalUpdate(List<RippleClient> clients) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Publishing.");
        // 1KB value
        String value = PayloadGenerator.generateKeyValuePair(16, 64);
        // clients.get(i).put("testApp", "test-" + i, WorkloadGenerator.generateKeyValuePair(16, 64));
        clients.get(0).put("testApp", "test", value);
    }

    public static void testIncrementalUpdate(UUID baseUuid, int publishCount, List<RippleClient> clients) {
        int i = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Publishing.");
        for (i = 0; i < publishCount; i++) {
            // 128 bytes
            String value = PayloadGenerator.generateKeyValuePair(2, 64);
            clients.get(i).incrementalUpdate("testApp", "test", baseUuid
                    , Constants.ATOMIC_OPERATION_ADD_ENTRY, value);
        }
    }

    public static void main(String[] args) {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
            int serverClusterSize = 10;
            int clientClusterSize = 1000;
            int qps = 10;
            int payloadSize = 10 * 1024;
            int topicSize = 1000;
            int duration = 120;

            int i = 0;
            List<RippleClient> rippleClients = new ArrayList<>();

            ExecutorService pool = Executors.newFixedThreadPool(clientClusterSize);
            for (i = 0; i < clientClusterSize; i++) {
                Files.createDirectories(Paths.get(DATABASE_PATH));
                String storageLocation = DATABASE_PATH + "\\" + UUID.randomUUID().toString() + ".db";
                RippleClient rippleClient = new RippleClient(CLUSTER_VM_LAB.subList(0, serverClusterSize), new HashingBasedSelector(new ModHashing(6, 200)), storageLocation);
                pool.submit(new StartTask(rippleClient, i + 1));
                rippleClients.add(rippleClient);
            }
            pool.shutdown();
            System.out.println("Done.");

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            testSubscribe(rippleClients);
            System.out.println("Subscribe done.");
            scanner.nextLine();

            WorkloadGenerator.runRippleLoadTest(qps, duration, payloadSize, topicSize, rippleClients);
            scanner.nextLine();

//            int publishCount = 20;
//            long startTime = System.nanoTime();
//            DispatchRequestHandler.StartTime = startTime;
//            SyncRequestHandler.StartTime = startTime;
//            initIncrementalUpdate(rippleClients);
//            scanner.nextLine();
//
//            Item item = rippleClients.get(0).get("testApp", "test");
//            List<AbstractMessage> list = rippleClients.get(0).getStorage().getMessageService().findMessages(item.getApplicationName(), item.getKey());
//            UUID baseUuid = list.get(0).getUuid();
//
//            System.out.println("Base uuid is: " + baseUuid);
//            scanner.nextLine();
//
//            int loops = 12;
//            for (i = 0; i < loops; i++) {
//                System.out.println("Loop " + (i + 1) + " start.");
//                startTime = System.nanoTime();
//                DispatchRequestHandler.StartTime = startTime;
//                SyncRequestHandler.StartTime = startTime;
//                // testPutAndGet(publishCount, rippleClients);
//                testIncrementalUpdate(baseUuid, publishCount, rippleClients);
//                System.out.println("Loop " + (i + 1) + " done.");
//                scanner.nextLine();
//            }

            testUnsubscribe(rippleClients);
            System.out.println("Unsubscribe done.");
            scanner.nextLine();

            System.out.println("Stopping clients.");

            pool = Executors.newFixedThreadPool(clientClusterSize);
            for (i = 0; i < clientClusterSize; i++) {
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
