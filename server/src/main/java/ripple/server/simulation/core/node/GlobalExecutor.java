package ripple.server.simulation.core.node;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author qingzhou.sjq
 */
public class GlobalExecutor {

    public static final long HEARTBEAT_INTERVAL_MS = TimeUnit.SECONDS.toMillis(5L);

    public static final long LEADER_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(15L);

    public static final long RANDOM_MS = TimeUnit.SECONDS.toMillis(5L);

    public static final long TICK_PERIOD_MS = TimeUnit.MILLISECONDS.toMillis(500L);

    public static final long ADDRESS_SERVER_UPDATE_INTERVAL_MS = TimeUnit.SECONDS.toMillis(5L);

    private static ScheduledExecutorService executorService;

    public GlobalExecutor() {
        executorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);

                t.setDaemon(true);
                t.setName("ripple.server.simulation.timer");

                return t;
            }
        });
    }


    public  void register(Runnable runnable) {
        executorService.scheduleAtFixedRate(runnable, 0, TICK_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    public  void registerDelay(Runnable runnable) {
        executorService.scheduleWithFixedDelay(runnable, 0, TICK_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    public  void register(Runnable runnable, long delay) {
        executorService.scheduleAtFixedRate(runnable, 0, delay, TimeUnit.MILLISECONDS);
    }
}
