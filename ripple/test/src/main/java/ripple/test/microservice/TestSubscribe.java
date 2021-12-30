package ripple.test.microservice;

import ripple.common.entity.NodeMetadata;
import ripple.server.RippleServer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TestSubscribe {
    private static final int SERVER_COUNT = 10;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DATABASE_PATH));

            List<RippleServer> serverList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();

            int branch = 2;
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
            int numberOne = 5;
            int numberTwo = 7;
            String oldFunction = "add";

            NumberService one = new NumberService(numberOne, nodeList, DATABASE_PATH + "\\number-service-1.db");
            one.start();
            System.out.println("[Number Service 1] " + one.getAddress() + ":" + one.getPort()
                    + ", Client = " + one.getClient().getUiAddress() + ":" + one.getClient().getUiPort());

            NumberService two = new NumberService(numberTwo, nodeList, DATABASE_PATH + "\\number-service-2.db");
            two.start();
            System.out.println("[Number Service 2] " + two.getAddress() + ":" + two.getPort()
                    + ", Client = " + two.getClient().getUiAddress() + ":" + two.getClient().getUiPort());

            OperatorService operator = new OperatorService(nodeList, DATABASE_PATH + "\\operator-service.db");
            operator.start();
            System.out.println("[Operator Service] " + operator.getAddress() + ":" + operator.getPort()
                    + ", Client = " + operator.getClient().getUiAddress() + ":" + operator.getClient().getUiPort());

            one.getClient().subscribe("testApp", "function");

            one.getClient().put("testApp", "oneAddress", one.getAddress() + ":" + one.getPort());
            one.getClient().put("testApp", "twoAddress", two.getAddress() + ":" + two.getPort());
            one.getClient().put("testApp", "function", oldFunction);

            System.out.println("Press any key to stop.");
            System.in.read();

            one.stop();
            two.stop();
            operator.stop();

            for (RippleServer rippleServer : serverList) {
                rippleServer.stop();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
