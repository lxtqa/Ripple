package ripple.agent.core.node.star;

import ripple.agent.core.BaseServlet;
import ripple.agent.core.Cluster;
import ripple.agent.core.Message;
import ripple.agent.core.node.AbstractNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class SyncServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(SyncServlet.class);

    public SyncServlet(AbstractNode node, Cluster cluster) {
        super(node, cluster);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        UUID messageUuid = UUID.fromString(request.getHeader("x-star-message-uuid"));
        logger.info("Sync: Receive message " + messageUuid + ".");
        this.getCluster().getMessageList().add(
                new Message(this.getNode(), messageUuid, "Sync: Receive message."));
    }
}
