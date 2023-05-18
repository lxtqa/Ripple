// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

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
                LOGGER.info("[Worker] Monitor CPU load.");
                this.getNode().updateCpuLoad(1000);
                LOGGER.info("[Worker] Current CPU load is {}.", this.getNode().getCurrentCpuLoad());
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
