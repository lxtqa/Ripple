package ripple.agent.core.node.star;

import ripple.agent.core.BaseServlet;
import ripple.agent.core.Cluster;
import ripple.agent.core.Message;
import ripple.agent.core.node.AbstractNode;
import ripple.agent.core.node.NodeMetadata;
import ripple.agent.helper.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class UpdateServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdateServlet.class);

    public UpdateServlet(AbstractNode node, Cluster cluster) {
        super(node, cluster);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        UUID messageUuid = UUID.fromString(request.getHeader("x-star-message-uuid"));
        logger.info("Update: Receive message " + messageUuid + ".");
        this.getCluster().getMessageList().add(
                new Message(this.getNode(), messageUuid, "Update: Begin."));
        Map<String, String> headers = new HashMap<>(1);
        headers.put("x-star-message-uuid", messageUuid.toString());
        for (NodeMetadata metadata : this.getCluster().getAllNodes().values()) {
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + StarEndpoint.SYNC;
            this.getCluster().getMessageList().add(
                    new Message(this.getNode(), messageUuid,
                            "Update: Send message to node " + metadata.getUuid()
                                    + " (" + metadata.getAddress() + ":" + metadata.getPort() + ")."));
            HttpHelper.get(url, headers);
        }
        this.getCluster().getMessageList().add(
                new Message(this.getNode(), messageUuid, "Update: Done."));
    }
}
