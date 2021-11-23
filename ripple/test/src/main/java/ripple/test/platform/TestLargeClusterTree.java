package ripple.test.platform;

import ripple.client.RippleClient;
import ripple.common.entity.Message;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class TestLargeClusterTree {
    private static final int SERVER_COUNT = 100;
    private static final int CLIENTS_PER_SERVER = 1;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
            String suffix = UUID.randomUUID().toString();
            String databasePath = DATABASE_PATH + "\\" + suffix;

            Files.createDirectories(Paths.get(databasePath));

            List<RippleServer> serverList = new ArrayList<>();
            List<RippleClient> clientList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();

            int branch = 4;
            int i = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                int serverId = i + 1;
                String storageLocation = databasePath + "\\server-" + serverId + ".db";
                RippleServer rippleServer = RippleServer.treeProtocol(serverId, storageLocation, branch);
                rippleServer.start();
                serverList.add(rippleServer);
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ", API port = " + rippleServer.getApiPort() + ", UI port = " + rippleServer.getUiPort());
                nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getApiPort()));
            }
            for (i = 0; i < SERVER_COUNT; i++) {
                serverList.get(i).initCluster(nodeList);
            }

            int j = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                for (j = 0; j < CLIENTS_PER_SERVER; j++) {
                    RippleServer rippleServer = serverList.get(i);
                    String serverAddress = rippleServer.getAddress();
                    int serverPort = rippleServer.getApiPort();
                    String storageLocation = databasePath + "\\server-" + rippleServer.getId() + "-client-" + (j + 1) + ".db";
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

            Date subscribeStartDate = new Date(System.currentTimeMillis());
            for (RippleClient rippleClient : clientList) {
                rippleClient.subscribe(applicationName, key);
            }
            Date subscribeEndDate = new Date(System.currentTimeMillis());
            System.out.println("Subscribe completed in " + (subscribeEndDate.getTime() - subscribeStartDate.getTime()) + " ms. (" + clientList.size() + " clients)");

            Date startDate = new Date(System.currentTimeMillis());
            System.out.println("Start update delivery");
            serverList.get(0).getNode().put(applicationName, key, value);
            Message message = serverList.get(0).getNode().getStorage().getMessageService()
                    .findMessages(applicationName, key).get(0);
            long count = serverList.get(0).getNode().getStorage().getAckService()
                    .getAck(message.getUuid()).getAckNodes().size();
            while (count != SERVER_COUNT) {
                System.out.println("Update sent to " + count + " server nodes. Wait for 100 ms.");
                Thread.sleep(100);
                count = serverList.get(0).getNode().getStorage().getAckService()
                        .getAck(message.getUuid()).getAckNodes().size();
            }
            Date endDate = new Date(System.currentTimeMillis());
            System.out.println("Delivery completed in " + (endDate.getTime() - startDate.getTime()) + " ms.");

            Date unsubscribeStartDate = new Date(System.currentTimeMillis());
            for (RippleClient rippleClient : clientList) {
                rippleClient.unsubscribe(applicationName, key);
            }
            Date unsubscribeEndDate = new Date(System.currentTimeMillis());
            System.out.println("Unsubscribe completed in " + (unsubscribeEndDate.getTime() - unsubscribeStartDate.getTime()) + " ms. (" + clientList.size() + " clients)");

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
