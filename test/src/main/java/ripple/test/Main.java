package ripple.test;

import ripple.client.RippleClient;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class Main {
    private static final int SERVER_COUNT = 10;
    private static final int CLIENTS_PER_SERVER = 5;
    private static final String SERVER_DATABASE_PATH = "D:\\ripple-test-dir\\server";
    private static final String CLIENT_DATABASE_PATH = "D:\\ripple-test-dir\\client";

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(SERVER_DATABASE_PATH));
            Files.createDirectories(Paths.get(CLIENT_DATABASE_PATH));

            List<RippleServer> serverList = new ArrayList<>();
            List<RippleClient> clientList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();
            int i = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                int serverId = i + 1;
                String storageLocation = SERVER_DATABASE_PATH + "\\server-" + serverId + ".txt";
                RippleServer rippleServer = RippleServer.starProtocol(serverId, storageLocation);
                rippleServer.start();
                serverList.add(rippleServer);
                System.out.println("Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ":" + rippleServer.getPort());
                nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getPort()));
            }
            for (i = 0; i < SERVER_COUNT; i++) {
                serverList.get(i).initCluster(nodeList);
            }

            String applicationName = "testApp";
            String key = "test";
            String value = "test";

            int j = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                for (j = 0; j < CLIENTS_PER_SERVER; j++) {
                    RippleServer rippleServer = serverList.get(i);
                    String serverAddress = rippleServer.getAddress();
                    int serverPort = rippleServer.getPort();
                    String storageLocation = CLIENT_DATABASE_PATH + "\\server-" + rippleServer.getId() + "-client-" + (j + 1) + ".txt";
                    RippleClient rippleClient = new RippleClient(serverAddress, serverPort, storageLocation);
                    rippleClient.start();
                    clientList.add(rippleClient);
                    System.out.println("Client " + (j + 1) + " for Server " + rippleServer.getId() + ":"
                            + rippleClient.getAddress() + ":" + rippleClient.getPort());
                }
            }

            for (RippleClient rippleClient : clientList) {
                rippleClient.subscribe(applicationName, key);
            }

            clientList.get(0).put(applicationName, key, value);

            for (RippleClient rippleClient : clientList) {
                rippleClient.unsubscribe(applicationName, key);
            }

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
