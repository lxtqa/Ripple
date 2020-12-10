package ripple.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Tracker {
    private Map<UUID, List<Integer>> pendingMessages;

    public Map<UUID, List<Integer>> getPendingMessages() {
        return pendingMessages;
    }

    public void setPendingMessages(Map<UUID, List<Integer>> pendingMessages) {
        this.pendingMessages = pendingMessages;
    }

    public Tracker() {
        this.setPendingMessages(new HashMap<>());
    }

    public void retrySending() {
        // TODO
    }

    public void recordAck(UUID messageUuid) {
        // TODO
    }

    public List<Integer> getProgress(UUID messageUuid) {
        // TODO
        return null;
    }
}
