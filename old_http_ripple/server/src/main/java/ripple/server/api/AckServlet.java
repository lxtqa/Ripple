package ripple.server.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Parameter;
import ripple.server.core.BaseServlet;
import ripple.server.core.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class AckServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AckServlet.class);

    public AckServlet(Node node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID messageUuid = UUID.fromString(request.getHeader(Parameter.UUID));
        int sourceId = Integer.parseInt(request.getHeader(Parameter.SOURCE_ID));
        int nodeId = Integer.parseInt(request.getHeader(Parameter.NODE_ID));

        LOGGER.info("[AckServlet] Receive ACK of {} from server {}.", messageUuid.toString(), nodeId);
        this.getNode().getTracker().recordAck(messageUuid, sourceId, nodeId);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(this.getNode().getObjectMapper().writeValueAsString(true));
    }
}
