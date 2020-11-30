package ripple.server.simulation.core.node.raft.server;

import com.alibaba.fastjson.JSON;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qingzhou.sjq
 */
public class GetPeerServlet extends RaftServlet{

    public GetPeerServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<RaftPeer> peerList = new ArrayList<RaftPeer>(peers.allPeers());
        RaftPeer peer = null;

        for (RaftPeer peer1 : peerList) {
            if (StringUtils.equals(peer1.ip, peers.getLocalServer())) {
                peer = peer1;
            }
        }

        if (peer == null) {
            peer = new RaftPeer();
            peer.ip = peers.getLocalServer();
        }

        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(JSON.toJSONString(peer));
        out.close();

    }
}
