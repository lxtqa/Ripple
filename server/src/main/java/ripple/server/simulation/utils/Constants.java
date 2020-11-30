package ripple.server.simulation.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/***************************************************************************
 *
 * @author qingzhou.sjq
 * @date 2019/1/3
 *
 ***************************************************************************/
public class Constants {

    public static final String STAR_UPDATE_PATH = "/star/update";

    public static final String STAR_SERVER_SYNC_PATH = "/star/sync";

    public static final String STAR_CLIETN_UPDATE_PATH = "/star/client/update";

    public static final String RAFT_VOTE_PATH = "/raft/vote";

    public static final String RAFT_BEAT_PATH = "/raft/beat";

    public static final String RAFT_PUB_PATH = "/raft/publish";

    public static final String RAFT_ON_PUB_PATH = "/raft/onPublish";

    public static final String RAFT_GET_PEER_PATH = "/raft/getPeer";

    public static final String ENGINE_REGISTRY_PREFIX = readEngineConfig() + ":8000";

    public static final String ENGINE_REGISTRY_AGENT = "/Api/Registry/Agent";

    public static final String ENGINE_REGISTRY_SERVER = "/Api/Registry/ServerNode";

    public static final String ENGINE_REGISTRY_CLIENT = "/Api/Registry/ClientNode";

//    public static final int LAYER_INTERNAL_CLUSTER_NUM = 3;

    public static final String LAYER_PUB_PATH = "/layer/publish";

    public static final String LAYER_ON_PUB_PATH = "/layer/onPublish";

    /**
     * 树的分叉数，将来要改为配置文件或者根据集群规模变更
     */
    public static final int TREE_TYPE = 2;

    public static final String TREE_PUB_PATH = "/tree/publish";

    public static final String TREE_ON_PUB_PATH = "/tree/onPublish";

    public static final String TREE_ACK_PATH = "/tree/ack";

    public static final int PUBLISH_TERM_INCREASE_COUNT = 100;

    public static final int INIT_LOCK_TIME_SECONDS = 3;

    public static final String ZIPKIN_DEFAULT_URL = "http://" + readEngineConfig() + ":9411/api/v2/spans";

    public static String readEngineConfig() {
//        File file = new File("src/main/resources/engineip.txt");
        InputStream in = Constants.class.getResourceAsStream("/engineip.txt");
        BufferedReader reader = null;
        String tempString = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            tempString = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return tempString;

    }
}
