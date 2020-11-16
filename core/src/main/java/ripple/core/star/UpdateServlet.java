package ripple.core.star;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.core.AbstractNode;
import ripple.core.BaseServlet;
import ripple.core.NodeMetadata;
import ripple.core.helper.HttpHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdateServlet.class);

    public UpdateServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        UUID messageUuid = UUID.fromString(request.getHeader("x-star-message-uuid"));
        logger.info("Update: Receive message " + messageUuid + ".");
        Map<String, String> headers = new HashMap<>(1);
        headers.put("x-star-message-uuid", messageUuid.toString());
        for (NodeMetadata metadata : this.getNode().getNodeList()) {
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + StarEndpoint.SYNC;
            HttpHelper.get(url, headers);
        }
    }
}
