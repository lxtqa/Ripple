package ripple.test;

import ripple.common.tcp.message.HeartbeatRequest;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;
import ripple.server.tcp.message.AckRequest;

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
public class Main {
    private static final int SERVER_COUNT = 2;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DATABASE_PATH));

            List<RippleServer> serverList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();

            int branch = 3;
            int i = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                int serverId = i + 1;
                String storageLocation = DATABASE_PATH + "\\server-" + serverId + ".db";
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

            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            heartbeatRequest.setUuid(UUID.randomUUID());
            Thread.sleep(1000);
            serverList.get(0).getNode().getApiServer().sendMessage(serverList.get(1).getAddress(), serverList.get(1).getApiPort(), heartbeatRequest);

            AckRequest ackRequest = new AckRequest();
            ackRequest.setUuid(UUID.randomUUID());
            ackRequest.setMessageUuid(UUID.randomUUID());
            ackRequest.setSourceId(1);
            ackRequest.setNodeId(2);
            serverList.get(0).getNode().getApiServer().sendMessage(serverList.get(1).getAddress(), serverList.get(1).getApiPort(), ackRequest);

            System.out.println("Press any key to stop.");
            System.in.read();

            for (RippleServer rippleServer : serverList) {
                rippleServer.stop();
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
