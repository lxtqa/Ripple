package ripple.server.tcp.handler;

import ripple.common.tcp.Handler;
import ripple.common.tcp.message.HeartbeatResponse;
import ripple.common.tcp.Message;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseHandler implements Handler {
    @Override
    public Message handle(Message message) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        System.out.println("Receive heartbeat response. uuid = "
                + heartbeatResponse.getUuid().toString()
                + ", success = " + heartbeatResponse.isSuccess());
        return null;
    }
}
