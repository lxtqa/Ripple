package ripple.client;

import ripple.client.entity.Item;
import ripple.client.helper.Api;

public class RippleClient {
    private String serverAddress;
    private int serverPort;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public RippleClient(String serverAddress, int serverPort) {
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
    }

    public Item get(String key) {
        return Api.get(this.getServerAddress(), this.getServerPort(), key);
    }

    public boolean put(String key, String value) {
        return Api.put(this.getServerAddress(), this.getServerPort(), key, value);
    }
}
