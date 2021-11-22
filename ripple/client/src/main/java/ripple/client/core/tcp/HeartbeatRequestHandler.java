package ripple.client.core.tcp;

import ripple.common.tcp.Handler;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.message.HeartbeatResponse;
import ripple.common.tcp.Message;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestHandler implements Handler {
    @Override
    public Message handle(Message message) {
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        System.out.println("Receive heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        System.out.println("Send heartbeat response.");
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);
        return heartbeatResponse;
    }
}
