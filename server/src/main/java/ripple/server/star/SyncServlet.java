package ripple.server.star;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.AbstractNode;
import ripple.server.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class SyncServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(SyncServlet.class);

    public SyncServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        UUID messageUuid = UUID.fromString(request.getHeader("x-star-message-uuid"));
        logger.info("Sync: Receive message " + messageUuid + ".");
    }
}
