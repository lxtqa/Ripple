package ripple.server.simulation.core.node.raft.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.utils.IoUtils;
import ripple.server.simulation.utils.Loggers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * @author qingzhou.sjq
 */
public class BeatServlet extends RaftServlet{

    public BeatServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String entity = null;
        try {
            entity = new String(IoUtils.tryDecompress(req.getInputStream()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");

        JSONObject json = JSON.parseObject(value);
        JSONObject beat = JSON.parseObject(json.getString("beat"));

        try {
            RaftPeer peer = receivedBeat(beat);
            resp.setContentType("application/json; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.write(JSON.toJSONString(peer));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  RaftPeer receivedBeat(JSONObject beat) throws Exception {

        final RaftPeer local = peers.local();
        final RaftPeer remote = new RaftPeer();
        remote.ip = beat.getJSONObject("peer").getString("ip");
        remote.state = RaftPeer.State.valueOf(beat.getJSONObject("peer").getString("state"));
        remote.term.set(beat.getJSONObject("peer").getLongValue("term"));
        remote.heartbeatDueMs = beat.getJSONObject("peer").getLongValue("heartbeatDueMs");
        remote.leaderDueMs = beat.getJSONObject("peer").getLongValue("leaderDueMs");
        remote.voteFor = beat.getJSONObject("peer").getString("voteFor");

        if (remote.state != RaftPeer.State.LEADER) {
            Loggers.RAFT.info("[RAFT] invalid state from master, state: {}, remote peer: {}",
                    remote.state, JSON.toJSONString(remote));
            throw new IllegalArgumentException("invalid state from master, state: " + remote.state);
        }

        if (local.term.get() > remote.term.get()) {
            Loggers.RAFT.info("[RAFT] out of date beat, beat-from-term: {}, beat-to-term: {}, remote peer: {}, and leaderDueMs: {}"
                    , remote.term.get(), local.term.get(), JSON.toJSONString(remote), local.leaderDueMs);
            throw new IllegalArgumentException("out of date beat, beat-from-term: " + remote.term.get()
                    + ", beat-to-term: " + local.term.get());
        }

        if (local.state != RaftPeer.State.FOLLOWER) {

            Loggers.RAFT.info("[RAFT] make remote as leader, remote peer: {}", JSON.toJSONString(remote));
            // mk follower
            local.state = RaftPeer.State.FOLLOWER;
            local.voteFor = remote.ip;
        }

        local.resetLeaderDue();
        local.resetHeartbeatDue();

        peers.makeLeader(remote);

        return local;
    }
}
