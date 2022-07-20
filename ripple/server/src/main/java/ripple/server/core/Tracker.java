package ripple.server.core;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Ack;
import ripple.common.entity.NodeMetadata;
import ripple.server.core.overlay.Overlay;
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
    private Overlay overlay;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public void setOverlay(Overlay overlay) {
        this.overlay = overlay;
    }

    public Tracker(Node node, Overlay overlay) {
        this.setNode(node);
        this.setOverlay(overlay);
    }

    private Map<UUID, AbstractMessage> getPendingMessages() {
        Map<UUID, AbstractMessage> map = new ConcurrentHashMap<>();
        List<Ack> ackList = this.getNode().getStorage().getAckService().getAllAcks();
        for (Ack ack : ackList) {
            AbstractMessage message = this.getNode().getStorage().getMessageService().getMessageByUuid(ack.getMessageUuid());
            map.put(message.getUuid(), message);
        }
        return map;
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
            AbstractMessage message = this.getPendingMessages().get(messageUuid);
            for (Integer id : nodeList) {
                NodeMetadata metadata = this.getNode().findServerById(id);
                LOGGER.info("[Tracker] Retry sync {} with server {}:{}.", message.getType(), metadata.getAddress(), metadata.getPort());
                Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
                Api.sync(channel, message);
                LOGGER.info("[Tracker] Record ACK of message {} from server {} to server {}.", message.getUuid(), metadata.getId(), message.getLastUpdateServerId());
                this.recordAck(message.getUuid(), message.getLastUpdateServerId(), metadata.getId());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void initProgress(AbstractMessage message) {
        LOGGER.info("[Tracker] Initialize ACK progress of message {} (application name = {}, key = {})."
                , message.getUuid(), message.getApplicationName(), message.getKey());
        this.getPendingMessages().put(message.getUuid(), message);
        List<Integer> nodeList = new ArrayList<>();
        List<NodeMetadata> toCollect = this.getOverlay().calculateNodesToCollectAck(message);
        for (NodeMetadata metadata : toCollect) {
            LOGGER.info("[Tracker] Attempting to collect ACK of message {} from server {}({}:{})."
                    , message.getUuid(), metadata.getId(), metadata.getAddress(), metadata.getPort());
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
            // Transfer ACK to the original sender
            LOGGER.info("[Tracker] Resend ACK of message {} from server {} to message source {}.", messageUuid, nodeId, sourceId);
            NodeMetadata metadata = this.getNode().findServerById(sourceId);
            Channel channel = this.getNode().getApiServer().findChannel(metadata.getAddress(), metadata.getPort());
            Api.ack(channel, messageUuid, sourceId, nodeId);
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
