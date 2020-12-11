package ripple.server.core.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.DeleteMessage;
import ripple.common.MessageType;
import ripple.common.UpdateMessage;
import ripple.server.core.BaseServlet;
import ripple.server.core.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class SyncServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServlet.class);

    public SyncServlet(Node node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = false;

        UUID uuid = UUID.fromString(request.getHeader("x-ripple-uuid"));
        String type = request.getHeader("x-ripple-type");
        String applicationName = request.getHeader("x-ripple-application-name");
        String key = request.getHeader("x-ripple-key");
        Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
        int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));

        if (type.equals(MessageType.UPDATE)) {
            String value = request.getHeader("x-ripple-value");
            LOGGER.info("[SyncServlet] Receive POST request. UUID = {}, Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , uuid, type, applicationName, key, value, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);
            UpdateMessage updateMessage = new UpdateMessage(uuid, applicationName, key, value, lastUpdate, lastUpdateServerId);
            result = this.getNode().handleMessage(updateMessage);
        } else if (type.equals(MessageType.DELETE)) {
            LOGGER.info("[SyncServlet] Receive POST request. UUID = {}, Type = {}, Application Name = {}, Key = {}, Last Update = {}, Last Update Server Id = {}."
                    , uuid, type, applicationName, key, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);
            DeleteMessage deleteMessage = new DeleteMessage(uuid, applicationName, key, lastUpdate, lastUpdateServerId);
            result = this.getNode().handleMessage(deleteMessage);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(this.getNode().getObjectMapper().writeValueAsString(result));
    }
}
