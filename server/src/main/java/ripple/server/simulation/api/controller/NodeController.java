package ripple.server.simulation.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ripple.server.simulation.core.node.NodeManager;

/**
 * @author fuxiao.tz
 */
@RestController("nodeController_api")
public class NodeController {
    private NodeManager nodeManager;

    private NodeManager getNodeManager() {
        return nodeManager;
    }

    @Autowired
    private void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    @RequestMapping(value = {"/Api/Node/CreateCluster"}, method = {RequestMethod.POST})
    public boolean createCluster(@RequestHeader("x-nacos-simulation-agent-type") String type
            , @RequestHeader("x-nacos-simulation-agent-server-count") int serverCount
            , @RequestHeader("x-nacos-simulation-agent-client-count") int clientCount) {
        return this.getNodeManager().createCluster(type, serverCount, clientCount);
    }

    @RequestMapping(value = {"/Api/Node/DisableNode"}, method = {RequestMethod.POST})
    public boolean disableNode(@RequestHeader("x-nacos-simulation-agent-node-disable") String uuid) {
        return this.getNodeManager().disableNode(uuid);
    }
}
