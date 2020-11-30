package ripple.server.simulation.core.node.raft.server;

import com.alibaba.fastjson.JSON;
import ripple.server.simulation.helper.HttpClient;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;
import java.util.*;

/**
 * @author qingzhou.sjq
 */
public class PeerSet {
    private RaftPeer leader = null;

    private String localServer = null;

    private static Map<String, RaftPeer> peers = new HashMap<String, RaftPeer>();

    private static Set<String> sites = new HashSet<>();

    public PeerSet(String server) {
        this.localServer = server;
    }

    public String getLocalServer() {
        return localServer;
    }

    public RaftPeer getLeader() {
        return leader;
    }

    public Set<String> allSites() {
        return sites;
    }

    public void add(List<String> servers) {
        for (String server : servers) {
            RaftPeer peer = new RaftPeer();
            peer.ip = server;

            peers.put(server, peer);
        }

    }

    public void renew(List<String> servers) {
        peers.clear();
        add(servers);
    }

    public void remove(List<String> servers) {
        for (String server : servers) {
            peers.remove(server);
        }
    }

    public RaftPeer update(RaftPeer peer) {
        peers.put(peer.ip, peer);
        return peer;
    }

    public boolean isLeader(String ip) {

        if (leader == null) {
            Loggers.RAFT.warn("[IS LEADER] no leader is available now!");
            return false;
        }

        return StringUtils.equals(leader.ip, ip);
    }

    public boolean isLeader() {
        return isLeader(localServer);
    }

    public Set<String> allServersIncludeMyself() {
        return peers.keySet();
    }

    public Set<String> allServersWithoutMySelf() {
        Set<String> servers = new HashSet<String>(peers.keySet());

        // exclude myself
        servers.remove(local().ip);

        return servers;
    }

    public Collection<RaftPeer> allPeers() {
        return peers.values();
    }

    public int size() {
        return peers.size();
    }

    public RaftPeer decideLeader(RaftPeer candidate) {
        peers.put(candidate.ip, candidate);

        SortedBag ips = new TreeBag();
        int maxApproveCount = 0;
        String maxApprovePeer = null;
        for (RaftPeer peer : peers.values()) {
            if (StringUtils.isEmpty(peer.voteFor)) {
                continue;
            }

            ips.add(peer.voteFor);
            if (ips.getCount(peer.voteFor) > maxApproveCount) {
                maxApproveCount = ips.getCount(peer.voteFor);
                maxApprovePeer = peer.voteFor;
            }
        }

        if (maxApproveCount >= majorityCount()) {
            RaftPeer peer = peers.get(maxApprovePeer);
            peer.state = RaftPeer.State.LEADER;

            if (!Objects.equals(leader, peer)) {
                leader = peer;
                Loggers.RAFT.info("{} has become the LEADER", leader.ip);
            }
        }

        return leader;
    }

    public RaftPeer makeLeader(RaftPeer candidate) {
        if (!Objects.equals(leader, candidate)) {
            leader = candidate;
            Loggers.RAFT.info("{} has become the LEADER, local: {}, leader: {}",
                    leader.ip, JSON.toJSONString(local()), JSON.toJSONString(leader));
        }

        for (final RaftPeer peer : peers.values()) {
            Map<String, String> params = new HashMap<String, String>(1);
            if (!Objects.equals(peer, candidate) && peer.state == RaftPeer.State.LEADER) {
                try {
                    String url = Util.buildURL(peer.ip, Constants.RAFT_GET_PEER_PATH);
                    HttpClient.asyncHttpPost(url, null, params, new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
                                Loggers.RAFT.error("[NACOS-RAFT] get peer failed: {}, peer: {}",
                                        response.getResponseBody(), peer.ip);
                                peer.state = RaftPeer.State.FOLLOWER;
                                return 1;
                            }

                            update(JSON.parseObject(response.getResponseBody(), RaftPeer.class));

                            return 0;
                        }
                    });
                } catch (Exception e) {
                    peer.state = RaftPeer.State.FOLLOWER;
                    Loggers.RAFT.error("[NACOS-RAFT] error while getting peer from peer: {}", peer.ip);
                }
            }
        }

        return update(candidate);
    }

    public RaftPeer local() {
        RaftPeer peer = peers.get(localServer);
        if (peer == null) {
            throw new IllegalStateException("unable to find local peer: " + localServer + ", all peers: "
                    + Arrays.toString(peers.keySet().toArray()));
        }

        return peer;
    }

    public RaftPeer get(String server) {
        return peers.get(server);
    }

    public int majorityCount() {
        return peers.size() / 2 + 1;
    }

    public void reset() {

        leader = null;

        for (RaftPeer peer : peers.values()) {
            peer.voteFor = null;
        }
    }

    public void setTerm(long term) {
        RaftPeer local = local();

        if (term < local.term.get()) {
            return;
        }

        local.term.set(term);
    }

    public long getTerm() {
        return local().term.get();
    }

    public boolean contains(RaftPeer remote) {
        return peers.containsKey(remote.ip);
    }
}
