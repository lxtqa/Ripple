package ripple.client.core.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.core.BaseServlet;
import ripple.common.DeleteMessage;
import ripple.common.Item;
import ripple.common.Message;
import ripple.common.MessageType;
import ripple.common.UpdateMessage;

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
        Item item = this.getClient().getStorage().get(message.getApplicationName(), message.getKey());
        boolean newItem = false;
        if (item == null) {
            item = new Item();
            newItem = true;
        }
        synchronized (this) {
            if (newItem) {
                item.setApplicationName(message.getApplicationName());
                item.setKey(message.getKey());
            }
            boolean exist = false;
            for (Message elem : item.getMessages()) {
                if (elem.getUuid().equals(message.getUuid())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                item.getMessages().add(message);
            }
        }
        this.getClient().getStorage().put(item);
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

        Message message = null;

        if (type.equals(MessageType.UPDATE)) {
            String value = request.getHeader("x-ripple-value");
            LOGGER.info("[SyncServlet] Receive request: UUID = {}, Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , uuid, type, applicationName, key, value, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);

            message = new UpdateMessage(uuid, applicationName, key, value, lastUpdate, lastUpdateServerId);
        } else if (type.equals(MessageType.DELETE)) {
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
