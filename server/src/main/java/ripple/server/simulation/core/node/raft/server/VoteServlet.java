package ripple.server.simulation.core.node.raft.server;

import com.alibaba.fastjson.JSON;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理申请选票的请求
 *
 * @author qingzhou.sjq
 */
public class VoteServlet extends RaftServlet{


    public VoteServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RaftPeer peer = receivedVote(
                JSON.parseObject(Util.required(req, "vote"), RaftPeer.class));
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(JSON.toJSONString(peer));
        out.close();
    }

    public RaftPeer receivedVote(RaftPeer remote) {
        if (!peers.contains(remote)) {
            throw new IllegalStateException("can not find peer: " + remote.ip);
        }

        RaftPeer local = peers.local();
        if (remote.term.get() <= local.term.get()) {
            String msg = "received illegitimate vote" +
                    ", voter-term:" + remote.term + ", voter-ip:" + remote.ip + ", votee-term:" + local.term;

            Loggers.RAFT.info(msg);
            if (StringUtils.isEmpty(local.voteFor)) {
                local.voteFor = local.ip;
            }

            return local;
        }

        local.resetLeaderDue();

        local.state = RaftPeer.State.FOLLOWER;
        local.voteFor = remote.ip;
        local.term.set(remote.term.get());

        Loggers.RAFT.info("vote {} as leader, term: {}", remote.ip, remote.term);

        return local;
    }
}
