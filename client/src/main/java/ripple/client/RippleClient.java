package ripple.client;

import ripple.client.callback.CallbackServer;
import ripple.client.entity.Item;
import ripple.client.helper.Api;

import java.util.concurrent.ConcurrentHashMap;

public class RippleClient {
    private String serverAddress;
    private int serverPort;
    private ConcurrentHashMap<String, Item> storage;
    private CallbackServer callbackServer;

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

    public ConcurrentHashMap<String, Item> getStorage() {
        return storage;
    }

    public void setStorage(ConcurrentHashMap<String, Item> storage) {
        this.storage = storage;
    }

    public CallbackServer getCallbackServer() {
        return callbackServer;
    }

    public void setCallbackServer(CallbackServer callbackServer) {
        this.callbackServer = callbackServer;
    }

    public RippleClient(String serverAddress, int serverPort) {
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
        this.setStorage(new ConcurrentHashMap<>());
        this.setCallbackServer(new CallbackServer(this));
    }

    public Item get(String key) {
        if (this.getStorage().containsKey(key)) {
            return this.getStorage().get(key);
        } else {
            Item item = Api.get(this.getServerAddress(), this.getServerPort(), key);
            this.getStorage().put(key, item);
            return item;
        }
    }

    public boolean put(String key, String value) {
        boolean result = Api.put(this.getServerAddress(), this.getServerPort(), key, value);
        this.get(key);
        return result;
    }

    public boolean startCallback() {
        return this.getCallbackServer().start();
    }

    public boolean stopCallback() {
        return this.getCallbackServer().stop();
    }
}
