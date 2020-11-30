package ripple.server.simulation.core.node;

import ripple.server.simulation.Main;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.layer.LayerNodeBuilder;
import ripple.server.simulation.core.node.raft.RaftNodeBuilder;
import ripple.server.simulation.core.node.star.StarNodeBuilder;
import ripple.server.simulation.core.node.tree.TreeNodeBuilder;
import ripple.server.simulation.helper.RegistryHelper;
import ripple.server.simulation.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@Component
public class NodeManager {
    private static final Logger logger = LoggerFactory.getLogger(NodeManager.class);

    private Context context;
    private Emulator emulator;
    private RegistryHelper registryHelper;

    public Context getContext() {
        return context;
    }

    @Autowired
    private void setContext(Context context) {
        this.context = context;
    }

    public Emulator getEmulator() {
        return emulator;
    }

    @Autowired
    private void setEmulator(Emulator emulator) {
        this.emulator = emulator;
    }

    public RegistryHelper getRegistryHelper() {
        return registryHelper;
    }

    @Autowired
    private void setRegistryHelper(RegistryHelper registryHelper) {
        this.registryHelper = registryHelper;
    }

    private NodeBuilder getNodeBuilder(String type) {
        if (NodeType.STAR.equals(type)) {
            return new StarNodeBuilder();
        }
        if (NodeType.RAFT.equals(type)) {
            return new RaftNodeBuilder();
        }
        if (NodeType.TREE.equals(type)) {
            return new TreeNodeBuilder();
        }
        if (NodeType.LAYER.equals(type)){
            return new LayerNodeBuilder();
        }

        // DEFAULT
        return new StarNodeBuilder();
    }

    public boolean disableNode(String uuid) {
        return this.getContext().getServerNodes().get(UUID.fromString(uuid)).stop();
    }

    public boolean createCluster(String type, int serverCount, int clientCount) {
        synchronized (this) {
            for (AbstractNode serverNode : this.getContext().getServerNodes().values()) {
                serverNode.stop();
                this.getRegistryHelper().unregisterServerNode(serverNode.getUuid());
            }
            this.getContext().getServerNodes().clear();
            for (AbstractNode clientNode : this.getContext().getClientNodes().values()) {
                clientNode.stop();
                this.getRegistryHelper().unregisterClientNode(clientNode.getUuid());
            }
            this.getContext().getClientNodes().clear();
            System.gc();

            NodeBuilder builder = this.getNodeBuilder(type);
            int i = 0;

            for (i = 0; i < clientCount; i++) {
                AbstractNode clientNode = builder.buildClientNode(this.getEmulator(), this.getContext());
                this.getContext().getClientNodes().put(clientNode.getUuid(), clientNode);
                logger.info("New Client Instance --- " + clientNode.getAddress() + ":" + clientNode.getPort());
                this.getRegistryHelper().registerClientNode(clientNode.getUuid(), Main.AGENT_UUID
                        , clientNode.getAddress(), clientNode.getPort());
            }
            // client为server的整数倍，余数个client将无法推送到；
            int clientPerServer = clientCount / serverCount;

            List<AbstractNode> clientNodes = new ArrayList<>(this.getContext().getClientNodes().values());
            List<String> clientIpList = new ArrayList<>();

            for (i = 0; i < serverCount; i++) {
                for (int j = i * clientPerServer; j < (i + 1) * clientPerServer; j++) {
                    AbstractNode clientNode = clientNodes.get(j);
                    clientIpList.add(Util.buildAddress(clientNode.getAddress(), clientNode.getPort()));
                }
                AbstractNode serverNode = builder.buildServerNode(this.getEmulator(), this.getContext(), new ArrayList<>(clientIpList));
                clientIpList.clear();
                this.getContext().getServerNodes().put(serverNode.getUuid(), serverNode);
                logger.info("New Server Instance --- " + serverNode.getAddress() + ":" + serverNode.getPort()
                        + " uuid: " +serverNode.getUuid());
                this.getRegistryHelper().registerServerNode(serverNode.getUuid(), Main.AGENT_UUID
                        , serverNode.getAddress(), serverNode.getPort());
            }
            for (AbstractNode serverNode : this.getContext().getServerNodes().values()) {
                serverNode.start();
            }


            return true;
        }
    }

    public boolean clearCluster() {
        synchronized (this) {
            for (AbstractNode serverNode : this.getContext().getServerNodes().values()) {
                serverNode.stop();
                this.getRegistryHelper().unregisterServerNode(serverNode.getUuid());
            }
            this.getContext().getServerNodes().clear();
            for (AbstractNode clientNode : this.getContext().getClientNodes().values()) {
                clientNode.stop();
                this.getRegistryHelper().unregisterClientNode(clientNode.getUuid());
            }
            this.getContext().getClientNodes().clear();
            System.gc();

            return true;
        }
    }


}
