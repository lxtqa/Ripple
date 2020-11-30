package ripple.server.simulation.core.node;

import java.util.UUID;

/**
 * @author qingzhou.sjq
 */
public class AbstractPeer {

    /**
     * 此处的ip在仿真系统里代表ip + port
     */
    public String ip;

    public UUID uuid;

    public AbstractPeer() {

    }

    public AbstractPeer(UUID uuid, String ip) {
        setIp(ip);
        setUuid(uuid);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractPeer other = (AbstractPeer) obj;
        if (this.ip.equals(other.ip) && this.uuid.equals(other.uuid)) {
            return true;
        }
        return false;
    }
}
