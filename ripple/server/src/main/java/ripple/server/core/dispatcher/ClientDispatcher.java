package ripple.server.core.dispatcher;

import ripple.common.entity.AbstractMessage;
import ripple.common.entity.ClientMetadata;

import java.util.Set;

/**
 * @author Zhen Tang
 */
public interface ClientDispatcher {
    boolean notifyClients(Set<ClientMetadata> clientList, AbstractMessage message);
}
