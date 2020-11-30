package ripple.server.simulation.core.node.raft.server;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qingzhou.sjq
 */
public class Datum {
    public String key;

    public String value;

    public AtomicLong timestamp = new AtomicLong(0L);
}
