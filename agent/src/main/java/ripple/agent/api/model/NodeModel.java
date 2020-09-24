package ripple.agent.api.model;

import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class NodeModel {
    private UUID uuid;
    private UUID agentUuid;
    private UUID clusterUuid;
    private String type;
    private String address;
    private int port;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getAgentUuid() {
        return agentUuid;
    }

    public void setAgentUuid(UUID agentUuid) {
        this.agentUuid = agentUuid;
    }

    public UUID getClusterUuid() {
        return clusterUuid;
    }

    public void setClusterUuid(UUID clusterUuid) {
        this.clusterUuid = clusterUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
