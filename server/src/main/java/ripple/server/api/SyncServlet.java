package ripple.server.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Parameter;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.MessageType;
import ripple.common.entity.UpdateMessage;
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

        UUID uuid = UUID.fromString(request.getHeader(Parameter.UUID));
        String type = request.getHeader(Parameter.TYPE);
        String applicationName = request.getHeader(Parameter.APPLICATION_NAME);
        String key = request.getHeader(Parameter.KEY);
        Date lastUpdate = new Date(Long.parseLong(request.getHeader(Parameter.LAST_UPDATE)));
        int lastUpdateServerId = Integer.parseInt(request.getHeader(Parameter.LAST_UPDATE_SERVER_ID));

        if (type.equals(MessageType.UPDATE)) {
            String value = request.getHeader(Parameter.VALUE);
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
