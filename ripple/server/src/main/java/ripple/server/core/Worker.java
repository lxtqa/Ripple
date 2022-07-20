package ripple.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhen Tang
 */
public class Worker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);
    private static final int INTERVAL = 10000;

    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Worker(Node node) {
        this.setNode(node);
    }

    @Override
    public void run() {
        while (this.getNode().isRunning() && !Thread.currentThread().isInterrupted()) {
            try {
                LOGGER.info("[Worker] Reconnecting.");
                this.getNode().reconnect(this.getNode().getNodeList());
                LOGGER.info("[Worker] Check health.");
                this.getNode().getHealthManager().checkHealth();
                LOGGER.info("[Worker] Sending pending messages.");
                this.getNode().getTracker().retry();
                Thread.sleep(INTERVAL);
            } catch (InterruptedException exception) {
                LOGGER.info("[Worker] Interrupted.");
                return;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
