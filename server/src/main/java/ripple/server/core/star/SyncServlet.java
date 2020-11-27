package ripple.server.core.star;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.*;
import ripple.server.helper.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class SyncServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public SyncServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationName = request.getHeader("x-ripple-application-name");
        String key = request.getHeader("x-ripple-key");
        String value = request.getHeader("x-ripple-value");
        Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
        int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));
        LOGGER.info("[SyncServlet] Receive request: Application Name = " + applicationName
                + ", Key = " + key
                + ", Value = " + value
                + ", Last Update = " + SimpleDateFormat.getDateTimeInstance().format(lastUpdate)
                + ", Last Update Server Id = " + lastUpdateServerId);

        // Update local storage
        Item item = this.getNode().getStorage().get(applicationName, key);
        if (item == null) {
            item = new Item();
        }
        synchronized (this) {
            item.setApplicationName(applicationName);
            item.setKey(key);
            item.setValue(value);
            item.setLastUpdate(lastUpdate);
            item.setLastUpdateServerId(lastUpdateServerId);
        }
        this.getNode().getStorage().put(item);

        ItemKey itemKey = new ItemKey(applicationName, key);
        // Notify clients
        if (this.getNode().getSubscription().containsKey(itemKey)) {
            List<ClientMetadata> clients = this.getNode().getSubscription().get(itemKey);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[SyncServlet] Notify client " + metadata.getAddress() + ":" + metadata.getPort() + ".");
                Api.notifyClient(metadata, item);
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(true));
    }
}
