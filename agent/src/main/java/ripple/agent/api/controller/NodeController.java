package ripple.agent.api.controller;

import ripple.agent.api.model.MessageModel;
import ripple.agent.api.model.NodeModel;
import ripple.agent.core.Message;
import ripple.agent.core.node.AbstractNode;
import ripple.agent.core.node.NodeManager;
import ripple.agent.core.node.NodeMetadata;
import ripple.agent.helper.ModelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@RestController
public class NodeController {
    private NodeManager nodeManager;
    private ModelHelper modelHelper;

    private NodeManager getNodeManager() {
        return nodeManager;
    }

    @Autowired
    private void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    private ModelHelper getModelHelper() {
        return modelHelper;
    }

    @Autowired
    private void setModelHelper(ModelHelper modelHelper) {
        this.modelHelper = modelHelper;
    }

    @RequestMapping(value = {"/Api/Node/CreateCluster"}, method = {RequestMethod.POST})
    public List<NodeModel> createCluster(@RequestHeader("x-ripple-agent-cluster-uuid") UUID clusterUuid
            , @RequestHeader("x-ripple-agent-node-type") String nodeType
            , @RequestHeader("x-ripple-agent-node-count") int nodeCount) {
        List<NodeModel> nodeList = new ArrayList<>();
        Collection<AbstractNode> localNodes = this.getNodeManager().createCluster(clusterUuid, nodeType, nodeCount);
        for (AbstractNode abstractNode : localNodes) {
            nodeList.add(this.getModelHelper().parseNodeMetadata(abstractNode.getMetadata()));
        }
        return nodeList;
    }

    @RequestMapping(value = {"/Api/Node/UpdateCluster"}, method = {RequestMethod.POST})
    public boolean updateCluster(@RequestHeader("x-ripple-agent-cluster-uuid") UUID clusterUuid
            , @RequestBody List<NodeModel> nodeList) {
        List<NodeMetadata> nodeMetadataList = new ArrayList<>();
        for (NodeModel nodeModel : nodeList) {
            nodeMetadataList.add(this.getModelHelper().parseNodeModel(nodeModel));
        }
        return this.getNodeManager().updateCluster(clusterUuid, nodeMetadataList);
    }

    @RequestMapping(value = {"/Api/Node/RemoveCluster"}, method = {RequestMethod.POST})
    public boolean removeCluster(@RequestHeader("x-ripple-agent-cluster-uuid") UUID clusterUuid) {
        return this.getNodeManager().removeCluster(clusterUuid);
    }

    @RequestMapping(value = {"/Api/Node/Message"}, method = {RequestMethod.GET})
    public List<MessageModel> getMessages(@RequestHeader("x-ripple-agent-cluster-uuid") UUID clusterUuid) {
        List<Message> messages = this.getNodeManager().getMessages(clusterUuid);
        int size = messages.size();
        int i;
        List<MessageModel> ret = new ArrayList<>();
        for (i = 0; i < size; i++) {
            ret.add(this.getModelHelper().parseMessage(messages.get(i)));
        }
        return ret;
    }

}
