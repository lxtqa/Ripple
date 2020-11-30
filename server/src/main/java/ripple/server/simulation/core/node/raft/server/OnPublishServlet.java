package ripple.server.simulation.core.node.raft.server;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.commons.io.IOUtils;

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
public class OnPublishServlet extends RaftServlet{
    public OnPublishServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "application/json; charset=" + Util.getAcceptEncoding(req));
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Content-Encode", "gzip");

        String entity = IOUtils.toString(req.getInputStream(), "UTF-8");

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");

        Loggers.RAFT.info("peer receive data from leader: {}", value);

        // TODO 发送至client端
        for (String clientIp : targetClient) {
            Loggers.RAFT.info("push to client: {}", clientIp);
        }

        getEmulator().databaseOperation();

        PrintWriter out = resp.getWriter();
        out.write("ok");
        out.close();
    }

}
