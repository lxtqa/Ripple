package ripple.server.star;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.AbstractNode;
import ripple.server.BaseServlet;
import ripple.server.ClientMetadata;
import ripple.server.NodeMetadata;
import ripple.server.core.Item;
import ripple.server.helper.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class PutServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(PutServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PutServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getHeader("x-ripple-key");
        String value = request.getHeader("x-ripple-value");
        LOGGER.info("[PutServlet] Receive request: Key = " + key + ", Value = " + value + ".");

        // Update local storage
        Item item = null;
        if (!this.getNode().getStorage().containsKey(key)) {
            item = new Item();
            this.getNode().getStorage().put(key, item);
        }
        synchronized (item = this.getNode().getStorage().get(key)) {
            item.setKey(key);
            item.setValue(value);
            item.setLastUpdate(new Date(System.currentTimeMillis()));
            item.setLastUpdateServerId(this.getNode().getId());
        }

        // Notify clients
        if (this.getNode().getSubscription().containsKey(key)) {
            List<ClientMetadata> clients = this.getNode().getSubscription().get(key);
            for (ClientMetadata metadata : clients) {
                LOGGER.info("[PutServlet] Notify client " + metadata.getAddress() + ":" + metadata.getPort() + ".");
                Api.notifyClient(metadata, item);
            }
        }

        // [Star protocol] Sync to all the other servers
        for (NodeMetadata metadata : this.getNode().getNodeList()) {
            if (metadata.getId() == this.getNode().getId()
                    && metadata.getAddress().equals(this.getNode().getAddress())
                    && metadata.getPort() == this.getNode().getPort()) {
                continue;
            }
            LOGGER.info("[PutServlet] Sync to server " + metadata.getAddress() + ":" + metadata.getPort() + ".");
            Api.syncToServer(metadata, item);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(true));
    }
}
