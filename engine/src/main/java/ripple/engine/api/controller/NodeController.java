package ripple.engine.api.controller;

import ripple.engine.api.model.NodeModel;
import ripple.engine.entity.Node;
import ripple.engine.helper.ModelHelper;
import ripple.engine.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@RestController
public class NodeController {
    private NodeService nodeService;
    private ModelHelper modelHelper;

    private NodeService getNodeService() {
        return nodeService;
    }

    @Autowired
    private void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    private ModelHelper getModelHelper() {
        return modelHelper;
    }

    @Autowired
    private void setModelHelper(ModelHelper modelHelper) {
        this.modelHelper = modelHelper;
    }

    @RequestMapping(value = {"/Api/Node/CreateCluster"}, method = {RequestMethod.POST})
    public List<NodeModel> createCluster(@RequestHeader("x-ripple-engine-cluster-uuid") UUID clusterUuid
            , @RequestHeader("x-ripple-engine-node-type") String nodeType
            , @RequestHeader("x-ripple-engine-node-count") int nodeCount) {
        List<NodeModel> nodeList = new ArrayList<>();
        List<Node> nodes = this.getNodeService().createCluster(clusterUuid, nodeType, nodeCount);
        for (Node node : nodes) {
            nodeList.add(this.getModelHelper().parseNode(node));
        }
        return nodeList;
    }

    @RequestMapping(value = {"/Api/Node/RemoveCluster"}, method = {RequestMethod.POST})
    public boolean removeCluster(@RequestHeader("x-ripple-engine-cluster-uuid") UUID clusterUuid) {
        return this.getNodeService().removeCluster(clusterUuid);
    }

}
