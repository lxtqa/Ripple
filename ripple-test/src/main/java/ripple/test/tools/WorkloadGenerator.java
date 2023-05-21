// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.test.tools;

import com.alibaba.nacos.api.config.ConfigService;
import ripple.client.RippleClient;

import java.util.Date;
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
            long startTime = System.currentTimeMillis();
            Random random = new Random();
            // Prepare
            System.out.println("Preparing");
            int j = 0;
            for (j = 0; j < existingKeyCount; j++) {
                RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
                // 1KB per entry
                client.put("testApp", "testKey-" + (j + 1), PayloadGenerator.generateKeyValuePair(16, 64));
            }
            System.out.println("Prepare done.");
            new Scanner(System.in).nextLine();

            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void runRippleAdaptiveLoadTest(int payloadSize, List<RippleClient> clientCluster) {
        try {
            long startTime = System.currentTimeMillis();
            Random random = new Random();

            int qps = 5;
            int duration = 30;
            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
            qps = 10;
            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
            qps = 20;
            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
            qps = 5;
            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
            qps = 15;
            doRunLoadTest(payloadSize, clientCluster, startTime, random, qps, duration);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void doRunLoadTest(int payloadSize, List<RippleClient> clientCluster, long startTime, Random random, int qps, int duration) throws InterruptedException {
        int sleepTime = 1000 / qps;
        int i = 0;
        for (i = 0; i < duration * qps; i++) {
            RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
            long currentTime = System.currentTimeMillis();
            String value = (currentTime - startTime) + " " + (int) Math.floor((currentTime - startTime + 0.0) / 1000) + " "
                    + currentTime + " " + PayloadGenerator.generateKeyValuePair(16, payloadSize / 16);
            client.put("testApp", "test", value);
            Thread.sleep(sleepTime);
        }
        System.out.println("Done.");
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
