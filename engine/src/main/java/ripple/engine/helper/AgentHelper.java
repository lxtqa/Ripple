package ripple.engine.helper;

import ripple.engine.entity.Agent;
import ripple.engine.entity.Message;
import ripple.engine.entity.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@Component
public class AgentHelper {
    private ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Node> createCluster(Agent agent, UUID clusterUuid, String nodeType, int nodeCount) {
        try {
            String url = "http://" + agent.getAddress() + ":" + agent.getPort() + "/Api/Node/CreateCluster";
            Map<String, String> headers = new HashMap<>(3);
            headers.put("x-ripple-agent-cluster-uuid", clusterUuid.toString());
            headers.put("x-ripple-agent-node-type", nodeType);
            headers.put("x-ripple-agent-node-count", Integer.toString(nodeCount));
            String content = HttpHelper.post(url, headers, "", ContentType.APPLICATION_JSON);
            return this.getObjectMapper().readValue(content
                    , this.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, Node.class));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean updateCluster(Agent agent, UUID clusterUuid, List<Node> nodeList) {
        try {
            String url = "http://" + agent.getAddress() + ":" + agent.getPort() + "/Api/Node/UpdateCluster";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("x-ripple-agent-cluster-uuid", clusterUuid.toString());
            String content = HttpHelper.post(url, headers, this.getObjectMapper().writeValueAsString(nodeList), ContentType.APPLICATION_JSON);
            return this.getObjectMapper().readValue(content, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean removeCluster(Agent agent, UUID clusterUuid) {
        try {
            String url = "http://" + agent.getAddress() + ":" + agent.getPort() + "/Api/Node/RemoveCluster";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("x-ripple-agent-cluster-uuid", clusterUuid.toString());
            String content = HttpHelper.post(url, headers, "", ContentType.APPLICATION_JSON);
            return this.getObjectMapper().readValue(content, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<Message> gatherMessage(Agent agent, UUID clusterUuid) {
        try {
            String url = "http://" + agent.getAddress() + ":" + agent.getPort() + "/Api/Node/Message";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("x-ripple-agent-cluster-uuid", clusterUuid.toString());
            String content = HttpHelper.get(url, headers);
            return this.getObjectMapper().readValue(content
                    , this.getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, Message.class));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
