// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.test.padres;

import ca.utoronto.msrg.padres.common.message.AdvertisementMessage;
import ca.utoronto.msrg.padres.common.message.PublicationMessage;
import ca.utoronto.msrg.padres.common.message.SubscriptionMessage;
import ca.utoronto.msrg.padres.common.message.parser.MessageFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestEngine {
    private static final String[] CLUSTER_VM_LAB = {
            "socket://192.168.2.11:1100/Broker1"
            , "socket://192.168.2.12:1100/Broker2"
            , "socket://192.168.2.13:1100/Broker3"
            , "socket://192.168.2.14:1100/Broker4"
            , "socket://192.168.2.15:1100/Broker5"
            , "socket://192.168.2.16:1100/Broker6"
            , "socket://192.168.2.17:1100/Broker7"
            , "socket://192.168.2.18:1100/Broker8"
            , "socket://192.168.2.19:1100/Broker9"
            , "socket://192.168.2.20:1100/Broker10"
            , "socket://192.168.2.21:1100/Broker11"
            , "socket://192.168.2.22:1100/Broker12"
            , "socket://192.168.2.23:1100/Broker13"
            , "socket://192.168.2.24:1100/Broker14"
            , "socket://192.168.2.25:1100/Broker15"
            , "socket://192.168.2.26:1100/Broker16"
            , "socket://192.168.2.27:1100/Broker17"
            , "socket://192.168.2.28:1100/Broker18"
            , "socket://192.168.2.29:1100/Broker19"
            , "socket://192.168.2.30:1100/Broker20"};

    public static void main(String[] args) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            int serverClusterSize = 10;
            int clientClusterSize = 100;
            int qps = 10;
            int payloadSize = 10 * 1024;
            int topicSize = 1000;
            int duration = 120;

            List<TestPadresClient> clients = new ArrayList<>();

            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Connecting.");

            int i = 0;
            for (i = 0; i < clientClusterSize; i++) {
                String brokerURI = CLUSTER_VM_LAB[i % serverClusterSize];
                String clientId = UUID.randomUUID().toString().replace("-", "");
                TestPadresClient client = new TestPadresClient(clientId);
                client.connect(brokerURI);
                clients.add(client);
            }

            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Subscribing.");

            for (i = 0; i < clientClusterSize; i++) {
                TestPadresClient client = clients.get(i);
                long startTime = System.nanoTime();
                client.subscribe(MessageFactory
                        .createSubscriptionFromString("[class,eq,'temp'],[content,str-contains,' '],[value,<,0]"));
                long endTime = System.nanoTime();
                System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                        + "] Subscribed: " + (endTime - startTime + 0.00) / 1000 / 1000 + "ms");
            }
            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Subscribed.");

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                    + "] Publishing.");
            WorkloadGenerator.runPadresLoadTest(qps, duration, payloadSize, topicSize, clients);
            scanner.nextLine();


            for (i = 0; i < clientClusterSize; i++) {
                clients.get(i).disconnectAll();
            }
            System.out.println("Done.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
