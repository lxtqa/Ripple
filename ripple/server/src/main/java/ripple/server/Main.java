package ripple.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 以默认端口启动，数据库位于同目录下：java -jar -Did="1" -Dprotocol="tree" -Dbranch="3" -Dnodes="[id]:[address]:[port],2:192.168.1.3:2345" ripple-server.jar
        // 以指定端口和数据库文件位置启动：java -DapiPort=xxx -DuiPort=xxx -DstorageLocation=xxx -jar ripple-server.jar

        String storageLocation = System.getProperty("storageLocation");
        if (storageLocation == null) {
            storageLocation = "database.sqlite";
        }

        String apiPort = System.getProperty("apiPort");
        if (apiPort == null) {
            apiPort = "0";
        }

        String uiPort = System.getProperty("uiPort");
        if (uiPort == null) {
            uiPort = "0";
        }

        String protocol = System.getProperty("protocol");
        if (protocol == null) {
            LOGGER.info("[Main] Missing parameter: protocol.");
            return;
        }

        String id = System.getProperty("id");
        if (id == null) {
            LOGGER.info("[Main] Missing parameter: id.");
            return;
        }

        String nodes = System.getProperty("nodes");
        if (nodes == null) {
            LOGGER.info("[Main] Missing parameter: nodes.");
            return;
        }

        RippleServer server = null;
        if (protocol.equals("tree")) {
            String branch = System.getProperty("branch");
            if (branch == null) {
                LOGGER.info("[Main] Missing parameter: branch for protocol tree.");
                return;
            }
            server = RippleServer.treeProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(apiPort), Integer.parseInt(uiPort), Integer.parseInt(branch));
        } else if (protocol.equals("star")) {
            server = RippleServer.starProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(apiPort), Integer.parseInt(uiPort));
        }

        if (server != null) {
            List<NodeMetadata> nodeList = new ArrayList<>();
            String[] address = nodes.split(",");
            for (String item : address) {
                String[] result = item.split(":");
                NodeMetadata metadata = new NodeMetadata(Integer.parseInt(result[0]), result[1], Integer.parseInt(result[2]));
                nodeList.add(metadata);
            }
            server.getNode().setNodeList(nodeList);
            server.start();
            LOGGER.info("[Main] Ripple Server started. The API port is {}. The UI port is {}. The storage location is {}."
                    , server.getApiPort(), server.getUiPort(), server.getNode().getStorage().getFileName());
        }
    }
}
