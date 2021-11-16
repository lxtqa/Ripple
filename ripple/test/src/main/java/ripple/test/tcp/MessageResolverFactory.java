package ripple.test.tcp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MessageResolverFactory {
    private static final MessageResolverFactory resolverFactory = new MessageResolverFactory();
    private static final List<Resolver> resolvers = new CopyOnWriteArrayList<>();

    private MessageResolverFactory() {
    }

    public static MessageResolverFactory getInstance() {
        return resolverFactory;
    }

    public void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    public Resolver getMessageResolver(Message message) {
        for (Resolver resolver : resolvers) {
            if (resolver.support(message)) {
                return resolver;
            }
        }

        throw new RuntimeException("cannot find resolver, message type: " + message.getMessageType());
    }

}
