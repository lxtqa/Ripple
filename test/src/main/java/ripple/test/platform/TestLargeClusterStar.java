package ripple.test.platform;

import ripple.client.RippleClient;
import ripple.common.entity.Message;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TestLargeClusterStar {
    private static final int SERVER_COUNT = 100;
    private static final int CLIENTS_PER_SERVER = 1;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            System.setProperty("ripple.debug", "true");
            System.setProperty("ripple.networkLatency", "50");
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");

            Files.createDirectories(Paths.get(DATABASE_PATH));

            List<RippleServer> serverList = new ArrayList<>();
            List<RippleClient> clientList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();

            int i = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                int serverId = i + 1;
                String storageLocation = DATABASE_PATH + "\\server-" + serverId + ".db";
                RippleServer rippleServer = RippleServer.starProtocol(serverId, storageLocation);
                rippleServer.start();
                serverList.add(rippleServer);
                System.out.println("Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ":" + rippleServer.getPort());
                nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getPort()));
            }
            for (i = 0; i < SERVER_COUNT; i++) {
                serverList.get(i).initCluster(nodeList);
            }

            int j = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                for (j = 0; j < CLIENTS_PER_SERVER; j++) {
                    RippleServer rippleServer = serverList.get(i);
                    String serverAddress = rippleServer.getAddress();
                    int serverPort = rippleServer.getPort();
                    String storageLocation = DATABASE_PATH + "\\server-" + rippleServer.getId() + "-client-" + (j + 1) + ".db";
                    RippleClient rippleClient = new RippleClient(serverAddress, serverPort, storageLocation);
                    rippleClient.start();
                    clientList.add(rippleClient);
                    System.out.println("Client " + (j + 1) + " for Server " + rippleServer.getId() + ":"
                            + rippleClient.getAddress() + ":" + rippleClient.getPort());
                }
            }

            String applicationName = "testApp";
            String key = "test";
            String value = "test";

            for (RippleClient rippleClient : clientList) {
                rippleClient.subscribe(applicationName, key);
            }

            Date startDate = new Date(System.currentTimeMillis());
            System.out.println("Start update delivery");
            serverList.get(0).getNode().put(applicationName, key, value);
            Message message = serverList.get(0).getNode().getStorage().getMessageService()
                    .findMessages(applicationName, key).get(0);
            long count = serverList.get(0).getNode().getStorage().getAckService()
                    .getAck(message.getUuid()).getAckNodes().stream().count();
            while (count != SERVER_COUNT) {
                System.out.println("Update sent to " + count + " nodes. Wait for 100 ms.");
                Thread.sleep(100);
                count = serverList.get(0).getNode().getStorage().getAckService()
                        .getAck(message.getUuid()).getAckNodes().stream().count();
            }
            Date endDate = new Date(System.currentTimeMillis());
            System.out.println("Delivery completed in " + (endDate.getTime() - startDate.getTime()) + " ms.");

            for (RippleClient rippleClient : clientList) {
                rippleClient.stop();
            }

            for (RippleServer rippleServer : serverList) {
                rippleServer.stop();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
