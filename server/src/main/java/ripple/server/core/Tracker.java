package ripple.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.Ack;
import ripple.common.entity.Message;
import ripple.server.helper.Api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class Tracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);

    private Node node;
    private Map<UUID, Message> pendingMessages;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<UUID, Message> getPendingMessages() {
        return pendingMessages;
    }

    public void setPendingMessages(Map<UUID, Message> pendingMessages) {
        this.pendingMessages = pendingMessages;
    }

    public Tracker(Node node) {
        this.setNode(node);
        this.setPendingMessages(new ConcurrentHashMap<>());
    }

    public void retry() {
        Set<UUID> keys = this.getPendingMessages().keySet();
        for (UUID uuid : keys) {
            this.retrySending(uuid);
        }
    }

    public void retrySending(UUID messageUuid) {
        try {
            Set<Integer> nodeList = this.getNodesToRetry(messageUuid);
            Message message = this.getPendingMessages().get(messageUuid);
            for (Integer id : nodeList) {
                NodeMetadata metadata = this.getNode().findServerById(id);
                LOGGER.info("[Tracker] Retry sync {} with server {}:{}.", message.getType(), metadata.getAddress(), metadata.getPort());
                boolean success = Api.sync(metadata.getAddress(), metadata.getPort(), message);
                if (success) {
                    LOGGER.info("[Tracker] Record ACK of message {} from server {} to server {}.", message.getUuid(), metadata.getId(), message.getLastUpdateServerId());
                    this.recordAck(message.getUuid(), message.getLastUpdateServerId(), metadata.getId());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void initProgress(Message message) {
        this.getPendingMessages().put(message.getUuid(), message);
        List<Integer> nodeList = new ArrayList<>();
        for (NodeMetadata metadata : this.getNode().getNodeList()) {
            nodeList.add(metadata.getId());
        }
        this.getNode().getStorage().getAckService().initAck(message.getUuid(), nodeList);
        this.getNode().getStorage().getAckService().recordAck(message.getUuid(), this.getNode().getId());
    }

    public void recordAck(UUID messageUuid, int sourceId, int nodeId) {
        if (this.getNode().getStorage().getAckService().getAck(messageUuid) != null) {
            // Update local progress
            LOGGER.info("[Tracker] Update local ACK progress of message {} from server {}.", messageUuid, nodeId);
            this.getNode().getStorage().getAckService().recordAck(messageUuid, nodeId);
        } else {
            // Send ACK to sender
            LOGGER.info("[Tracker] Resend ACK of message {} from server {} to message source {}.", messageUuid, nodeId, sourceId);
            NodeMetadata metadata = this.getNode().findServerById(sourceId);
            Api.ack(metadata.getAddress(), metadata.getPort(), messageUuid, sourceId, nodeId);
        }
    }

    public Set<Integer> getNodesToRetry(UUID messageUuid) {
        if (!this.getPendingMessages().containsKey(messageUuid)) {
            return null;
        }
        Ack ack = this.getNode().getStorage().getAckService().getAck(messageUuid);

        Set<Integer> ret = new HashSet<>();
        for (Integer id : ack.getNodeList()) {
            if (!ack.getAckNodes().contains(id)) {
                ret.add(id);
            }
        }
        return ret;
    }
}
