package ripple.common.tcp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandlerFactory {
    private List<Handler> handlerList;

    private List<Handler> getHandlerList() {
        return handlerList;
    }

    private void setHandlerList(List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

    public void registerHandler(Handler handler) {
        this.getHandlerList().add(handler);
    }

    public HandlerFactory() {
        this.setHandlerList(new CopyOnWriteArrayList<>());
    }

    public Handler findHandler(Message message) {
        System.out.println("find handler");

        for (Handler handler : this.getHandlerList()) {
            if (handler.canHandle(message)) {
                return handler;
            }
        }

        throw new RuntimeException("Cannot find handler for message type: " + message.getType());
    }
}
