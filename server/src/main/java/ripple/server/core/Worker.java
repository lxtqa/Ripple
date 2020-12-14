package ripple.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhen Tang
 */
public class Worker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);
    private static final int INTERVAL = 1000;

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
        try {
            while (!Thread.currentThread().isInterrupted()) {
                LOGGER.info("[Worker] Check health.");
                this.getNode().getHealthManager().checkHealth();
                LOGGER.info("[Worker] Retry sending messages.");
                this.getNode().getTracker().retry();
                Thread.sleep(INTERVAL);
            }
            LOGGER.info("[Worker] Interrupted.");
        } catch (Exception exception) {

        }
    }
}
