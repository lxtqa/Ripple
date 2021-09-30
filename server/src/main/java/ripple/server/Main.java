package ripple.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 以默认端口启动，数据库位于同目录下：java -jar -Did=1 -Dprotocol=tree -Dbranch=3 ripple-server.jar
        // 以指定端口和数据库文件位置启动：java -Dport=xxx -DstorageLocation=xxx -jar ripple-server.jar

        String storageLocation = System.getProperty("storageLocation");
        if (storageLocation == null) {
            storageLocation = "database.sqlite";
        }

        String port = System.getProperty("port");
        String protocol = System.getProperty("protocol");
        String id = System.getProperty("id");

        RippleServer server = null;
        if (port != null) {
            if (protocol != null && protocol.equals("tree")) {
                int branch = Integer.parseInt(System.getProperty("branch"));
                server = RippleServer.treeProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(port), branch);
            } else if (protocol != null && protocol.equals("star")) {
                server = RippleServer.starProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(port));
            }
        } else {
            if (protocol != null && protocol.equals("tree")) {
                int branch = Integer.parseInt(System.getProperty("branch"));
                server = RippleServer.treeProtocol(Integer.parseInt(id), storageLocation, branch);
            } else if (protocol != null && protocol.equals("star")) {
                server = RippleServer.starProtocol(Integer.parseInt(id), storageLocation);
            }
        }
        if (server != null) {
            server.start();
            LOGGER.info("[Main] Ripple Server started. The port is {}. The storage location is {}."
                    , server.getPort(), server.getNode().getStorage().getFileName());
        }
    }
}
