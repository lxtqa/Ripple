package ripple.engine.entity;

import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class Agent {
    private UUID uuid;
    private String address;
    private int port;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
