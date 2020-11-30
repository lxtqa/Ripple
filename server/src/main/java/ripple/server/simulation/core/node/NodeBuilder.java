package ripple.server.simulation.core.node;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;

import java.util.List;

/**
 * @author fuxiao.tz
 */
public interface NodeBuilder {
    /**
     * 创建仿真服务器节点
     *
     * @param emulator 仿真器
     * @param context  上下文环境
     * @param targetClients server对应的client节点
     * @return 仿真服务器节点
     */
    AbstractNode buildServerNode(Emulator emulator, Context context, List<String> targetClients);

    /**
     * 创建仿真客户端节点
     *
     * @param emulator 仿真器
     * @param context  上下文环境
     * @return 仿真客户端节点
     */
    AbstractNode buildClientNode(Emulator emulator, Context context);
}
