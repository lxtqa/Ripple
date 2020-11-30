package ripple.server.simulation.core.node.star.server;

import brave.Tracing;
import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.AbstractPeer;
import ripple.server.simulation.helper.HttpHelper;
import ripple.server.simulation.helper.RegistryHelper;
import ripple.server.simulation.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 处理客户端的更新请求：写入数据库，推送至其他服务器节点，推送至客户端节点
 *
 * @author qingzhou.sjq
 * @author songao
 */
public class UpdateServlet extends StarServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateServlet.class);

    public UpdateServlet(Emulator emulator, Context context) {
        super(emulator, context);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 写入数据库，推送至其他服务器节点，推送至客户端节点
        getEmulator().databaseOperation();
        logger.info("write db, start sync... Host----" + req.getServerName() + ":" + req.getServerPort());
        for (AbstractPeer peer : serverPeers) {
            String url = "http://" + peer.ip + Constants.STAR_SERVER_SYNC_PATH;
            Tracing tracing = (Tracing) req.getServletContext().getAttribute("TRACING");
            HttpHelper.get(url, new HashMap<>(),tracing);
        }
        for (String clientIp : targetClient) {
            logger.info("push to client: {}", clientIp);
        }
        logger.info("sync finished.");
    }


}
