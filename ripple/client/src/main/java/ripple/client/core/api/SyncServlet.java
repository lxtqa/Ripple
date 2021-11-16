package ripple.client.core.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.core.BaseServlet;
import ripple.common.Parameter;
import ripple.common.entity.*;

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
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public SyncServlet(RippleClient client) {
        super(client);
    }

    private void applyMessage(Message message) {
        String applicationName = message.getApplicationName();
        String key = message.getKey();
        Item item = this.getClient().getStorage().getItemService().getItem(applicationName, key);
        if (item == null) {
            this.getClient().getStorage().getItemService().newItem(applicationName, key);
        }
        this.getClient().getStorage().getMessageService().newMessage(message);
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

        Message message = null;

        if (type.equals(Constants.MESSAGE_TYPE_UPDATE)) {
            String value = request.getHeader(Parameter.VALUE);
            LOGGER.info("[SyncServlet] Receive request: UUID = {}, Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , uuid, type, applicationName, key, value, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);

            message = new UpdateMessage(uuid, applicationName, key, value, lastUpdate, lastUpdateServerId);
        } else if (type.equals(Constants.MESSAGE_TYPE_DELETE)) {
            LOGGER.info("[SyncServlet] Receive request: UUID = {}, Type = {}, Application Name = {}, Key = {}, Last Update = {}, Last Update Server Id = {}."
                    , uuid, type, applicationName, key, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);
            message = new DeleteMessage(uuid, applicationName, key, lastUpdate, lastUpdateServerId);
        }

        this.applyMessage(message);
        result = true;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(result));
    }
}
