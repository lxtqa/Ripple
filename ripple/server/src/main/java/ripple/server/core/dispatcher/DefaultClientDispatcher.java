package ripple.server.core.dispatcher;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.ClientMetadata;
import ripple.server.core.Node;
import ripple.server.helper.Api;

import java.util.Set;

/**
 * @author Zhen Tang
 */
public class DefaultClientDispatcher implements ClientDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientDispatcher.class);
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public DefaultClientDispatcher(Node node) {
        this.setNode(node);
    }

    @Override
    public boolean notifyClients(Set<ClientMetadata> clientList, AbstractMessage message) {
        for (ClientMetadata metadata : clientList) {
            LOGGER.info("[DefaultClientDispatcher] Node-{}: Notify {} message ({}) to client {}:{}."
                    , this.getNode().getId(), message.getType(), message.getUuid(), metadata.getAddress(), metadata.getPort());
            Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
            Api.sync(channel, message);
        }
        return true;
    }
}
