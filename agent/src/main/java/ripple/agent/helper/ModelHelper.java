package ripple.agent.helper;

import ripple.agent.api.model.MessageModel;
import ripple.agent.api.model.NodeModel;
import ripple.agent.core.Message;
import ripple.agent.core.node.NodeMetadata;
import org.springframework.stereotype.Component;

/**
 * @author fuxiao.tz
 */
@Component
public class ModelHelper {
    public NodeModel parseNodeMetadata(NodeMetadata metadata) {
        NodeModel nodeModel = new NodeModel();
        nodeModel.setUuid(metadata.getUuid());
        nodeModel.setAgentUuid(metadata.getAgentUuid());
        nodeModel.setClusterUuid(metadata.getClusterUuid());
        nodeModel.setType(metadata.getType());
        nodeModel.setAddress(metadata.getAddress());
        nodeModel.setPort(metadata.getPort());
        return nodeModel;
    }

    public NodeMetadata parseNodeModel(NodeModel nodeModel) {
        NodeMetadata nodeMetadata = new NodeMetadata();
        nodeMetadata.setUuid(nodeModel.getUuid());
        nodeMetadata.setAgentUuid(nodeModel.getAgentUuid());
        nodeMetadata.setClusterUuid(nodeModel.getClusterUuid());
        nodeMetadata.setType(nodeModel.getType());
        nodeMetadata.setAddress(nodeModel.getAddress());
        nodeMetadata.setPort(nodeModel.getPort());
        return nodeMetadata;
    }

    public MessageModel parseMessage(Message message) {
        MessageModel messageModel = new MessageModel();
        messageModel.setClusterUuid(message.getClusterUuid());
        messageModel.setContent(message.getContent());
        messageModel.setDate(message.getDate());
        messageModel.setMessageUuid(message.getMessageUuid());
        messageModel.setNodeUuid(message.getNodeUuid());
        return messageModel;
    }
}
