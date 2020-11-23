package ripple.server.core;

public final class NodeMetadata {
    private int id;
    private String address;
    private int port;

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

    public NodeMetadata(int id, String address, int port) {
        this.setId(id);
        this.setAddress(address);
        this.setPort(port);
    }
}
