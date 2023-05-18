package ripple.common.entity;

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public final class NodeMetadata {
    private int id;
    private String address;
    private int port;

    public NodeMetadata(int id, String address, int port) {
        this.setId(id);
        this.setAddress(address);
        this.setPort(port);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeMetadata that = (NodeMetadata) o;
        return id == that.id &&
                port == that.port &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, port);
    }
}
