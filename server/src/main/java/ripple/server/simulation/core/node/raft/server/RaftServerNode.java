package ripple.server.simulation.core.node.raft.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.AbstractPeer;
import ripple.server.simulation.core.node.GlobalExecutor;
import ripple.server.simulation.helper.HttpClient;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPOutputStream;

/**
 * @author fuxiao.tz
 */
public class RaftServerNode extends AbstractNode {

    private PeerSet peers;


    private volatile ConcurrentMap<String, Datum> datums = new ConcurrentHashMap<String, Datum>();

    private Lock lock = new ReentrantLock();

    private volatile boolean initialized = false;

    private Context context;

    private List<RaftServlet> servlets;

    private List<String> targetClient;

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        servlets = new ArrayList<>(8);
        RaftServlet voteServlet = new VoteServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(voteServlet), Constants.RAFT_VOTE_PATH);
        RaftServlet beatServlet = new BeatServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(beatServlet), Constants.RAFT_BEAT_PATH);
        RaftServlet publishServlet = new PublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(publishServlet), Constants.RAFT_PUB_PATH);
        RaftServlet getPeerServlet = new GetPeerServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(getPeerServlet), Constants.RAFT_GET_PEER_PATH);
        OnPublishServlet onPublishServlet = new OnPublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(onPublishServlet), Constants.RAFT_ON_PUB_PATH);

        servlets.add(voteServlet);
        servlets.add(beatServlet);
        servlets.add(publishServlet);
        servlets.add(getPeerServlet);
        servlets.add(onPublishServlet);

    }

    public RaftServerNode(Emulator emulator, Context context, List<String> addressList) {
        super(emulator, context);
        this.context = context;
        targetClient = new ArrayList<>(addressList);
        peers = new PeerSet(Util.buildAddress(getAddress(), getPort()));
    }

    @Override
    public void start() {
        initPeers();
        executor.register(new GetClusterAddress(), GlobalExecutor.ADDRESS_SERVER_UPDATE_INTERVAL_MS);

        if (peers.size() > 0) {
            try {
                if (lock.tryLock(Constants.INIT_LOCK_TIME_SECONDS, TimeUnit.SECONDS)) {
                    initialized = true;
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new Exception("peers is empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (RaftServlet servlet : servlets) {
            servlet.setPeers(peers);
            servlet.setTargetClient(targetClient);
        }

        Loggers.RAFT.info("server start, peer count: {}, datum count: {}, current term: {}",
                peers.size(), datums.size(), peers.getTerm());


        executor.register(new MasterElection());
        executor.registerDelay(new HeartBeat());
    }

    @Override
    public void initPeers() {
        List<AbstractPeer> peerListFromEngine = registryHelper.getServerListFromEngine();
        if (localPeerList != null && localPeerList.equals(peerListFromEngine)) {
            return;
        }
        Loggers.RAFT.info("get new cluster config");
        localPeerList = peerListFromEngine;
        List<String> serverList = new ArrayList<>();
        if (peerListFromEngine.size() == 0) {
            try {
                throw new Exception("peerListFromEngine is empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (AbstractPeer node : peerListFromEngine) {
            serverList.add(node.ip);
        }
        peers.renew(serverList);
        serverList.clear();
    }

    public class MasterElection implements Runnable {
        @Override
        public void run() {
            try {
                RaftPeer local = peers.local();
                local.leaderDueMs -= GlobalExecutor.TICK_PERIOD_MS;
                if (local.leaderDueMs > 0) {
                    return;
                }

                // reset timeout
                local.resetLeaderDue();
                local.resetHeartbeatDue();

                sendVote();
            } catch (Exception e) {
                Loggers.RAFT.warn("[RAFT] error while master election {}", e);
            }

        }

        public void sendVote() {

            RaftPeer local = peers.get(Util.buildAddress(getAddress(), getPort()));
            Loggers.RAFT.info("leader timeout, start voting,leader: {}, term: {}, local: {}, peers.local(): {}",
                    JSON.toJSONString(getLeader()), local.term, local.ip, peers.local().ip);

            peers.reset();

            local.term.incrementAndGet();
            local.voteFor = local.ip;
            local.state = RaftPeer.State.CANDIDATE;

            Map<String, String> params = new HashMap<String, String>(1);
            params.put("vote", JSON.toJSONString(local));
            for (final String server : peers.allServersWithoutMySelf()) {
                Loggers.RAFT.info("{} request vote from: {}", local.ip, server);
                final String url = Util.buildURL(server, Constants.RAFT_VOTE_PATH);
                try {
                    HttpClient.asyncHttpPost(url, null, params, new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
                                Loggers.RAFT.error("NACOS-RAFT vote failed: {}, url: {}", response.getResponseBody(), url);
                                return 1;
                            }

                            RaftPeer peer = JSON.parseObject(response.getResponseBody(), RaftPeer.class);

                            Loggers.RAFT.info("{} received approve from peer: {}", local.ip, JSON.toJSONString(peer));

                            peers.decideLeader(peer);

                            return 0;
                        }
                    });
                } catch (Exception e) {
                    Loggers.RAFT.warn("error while sending vote to server: {}", server);
                }
            }
        }
    }





    public class HeartBeat implements Runnable {
        @Override
        public void run() {
            try {
                RaftPeer local = peers.local();
                local.heartbeatDueMs -= GlobalExecutor.TICK_PERIOD_MS;
                if (local.heartbeatDueMs > 0) {
                    return;
                }

                local.resetHeartbeatDue();

                sendBeat();
            } catch (Exception e) {
                Loggers.RAFT.warn("[RAFT] error while sending beat {}", e);
            }

        }

        public  void sendBeat() throws IOException, InterruptedException {
            RaftPeer local = peers.local();
            if (local.state != RaftPeer.State.LEADER) {
                return;
            }

            Loggers.RAFT.info("i am leader, local: {}, leader: {}", peers.local().ip, peers.getLeader().ip);

            Loggers.RAFT.info("[RAFT] send beat with {} keys.", datums.size());

            local.resetLeaderDue();

            // build data
            JSONObject packet = new JSONObject();
            packet.put("peer", local);

            JSONArray array = new JSONArray();

            packet.put("datums", array);
            // broadcast
            Map<String, String> params = new HashMap<String, String>(1);
            params.put("beat", JSON.toJSONString(packet));

            String content = JSON.toJSONString(params);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(content.getBytes("UTF-8"));
            gzip.close();

            byte[] compressedBytes = out.toByteArray();
            String compressedContent = new String(compressedBytes, "UTF-8");
            Loggers.RAFT.info("raw beat data size: {}, size of compressed data: {}",
                    content.length(), compressedContent.length());

            for (final String server : peers.allServersWithoutMySelf()) {
                try {
                    final String url = Util.buildURL(server, Constants.RAFT_BEAT_PATH);
                    Loggers.RAFT.info("send beat to server " + server);
                    HttpClient.asyncHttpPostLarge(url, null, compressedBytes, new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
                                Loggers.RAFT.error("NACOS-RAFT beat failed: {}, peer: {}",
                                        response.getResponseBody(), server);
                                return 1;
                            }

                            peers.update(JSON.parseObject(response.getResponseBody(), RaftPeer.class));
                            Loggers.RAFT.info("receive beat response from: {}", url);
                            return 0;
                        }

                        @Override
                        public void onThrowable(Throwable t) {
                            Loggers.RAFT.error("NACOS-RAFT error while sending heart-beat to peer: {} {}", server, t);
                        }
                    });
                } catch (Exception e) {
                    Loggers.RAFT.error("VIPSRV error while sending heart-beat to peer: {} {}", server, e);
                }
            }

        }


    }

    public void setTerm(long term) {
        peers.setTerm(term);
    }

    public long getTerm() {
        return peers.getTerm();
    }


    public boolean isLeader(String ip) {
        return peers.isLeader(ip);
    }

    public boolean isLeader() {
        return peers.isLeader("NetUtils.localServer()");
    }

    public Datum getDatum(String key) {
        return datums.get(key);
    }

    public RaftPeer getLeader() {
        return peers.getLeader();
    }

    public List<RaftPeer> getPeers() {
        return new ArrayList<RaftPeer>(peers.allPeers());
    }

    public PeerSet getPeerSet() {
        return peers;
    }

    public void setPeerSet(PeerSet peerSet) {
        peers = peerSet;
    }



}
