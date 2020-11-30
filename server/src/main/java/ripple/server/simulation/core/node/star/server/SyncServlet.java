package ripple.server.simulation.core.node.star.server;

import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 读取数据库，推送至客户端节点
 *
 * @author qingzhou.sjq
 */
public class SyncServlet extends StarServlet {

    private static final Logger logger = LoggerFactory.getLogger(SyncServlet.class);


    public SyncServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 读取数据库，推送至客户端节点
        logger.info("Recv sync msg. Host---" + req.getServerName() + ":" + req.getServerPort());
        getEmulator().databaseOperation();
        for (String clientIp : targetClient) {
            logger.info("push to client: {}", clientIp);
        }
    }
}
