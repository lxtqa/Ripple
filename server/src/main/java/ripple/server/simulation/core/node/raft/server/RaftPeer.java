package ripple.server.simulation.core.node.raft.server;

import ripple.server.simulation.core.node.AbstractPeer;
import ripple.server.simulation.core.node.GlobalExecutor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qingzhou.sjq
 */
public class RaftPeer extends AbstractPeer{

    /**
     * 此处的ip在仿真系统里代表ip + port
     */
    public String voteFor;

    public AtomicLong term = new AtomicLong(0L);

    public volatile long leaderDueMs = RandomUtils.nextLong(0, GlobalExecutor.LEADER_TIMEOUT_MS);

    public volatile  long heartbeatDueMs = RandomUtils.nextLong(0, GlobalExecutor.HEARTBEAT_INTERVAL_MS);

    public State state = State.FOLLOWER;

    public void resetLeaderDue() {
        leaderDueMs = GlobalExecutor.LEADER_TIMEOUT_MS + RandomUtils.nextLong(0, GlobalExecutor.RANDOM_MS);
    }

    public void resetHeartbeatDue() {
        heartbeatDueMs = GlobalExecutor.HEARTBEAT_INTERVAL_MS;
    }

    public enum State {
        /**
         * Leader of the cluster, only one leader stands in a cluster
         */
        LEADER,
        /**
         * Follower of the cluster, report to and copy from leader
         */
        FOLLOWER,
        /**
         * Candidate leader to be elected
         */
        CANDIDATE
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof RaftPeer)) {
            return false;
        }

        RaftPeer other = (RaftPeer) obj;

        return StringUtils.equals(ip, other.ip);
    }
}
