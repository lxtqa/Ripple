package ripple.test.remote;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * @author Zhen Tang
 */
public class TestNacos {

    private static long startTime;

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime() {
        startTime = System.nanoTime();
    }

    public static void main(String[] args) {
        try {
            String serverAddr = "192.168.2.21";
            String dataId = "nacos.cfg.dataId";
            String group = "test";

            int count = 100;
            List<ConfigService> clients = new ArrayList<>();

            // Nacos限流，默认每秒5次
            int i = 0;
            for (i = 0; i < count; i++) {
                ConfigService configService = NacosFactory.createConfigService(serverAddr);
                configService.addListener(dataId, group, new Listener() {
                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        long endTime = System.nanoTime();
                        System.out.println("recieve: dataId = " + dataId + ", group = " + group + ", content = " + configInfo
                                + ", time = " + (endTime - getStartTime() + 0.00) / 1000 / 1000 + "ms");
                    }

                    @Override
                    public Executor getExecutor() {
                        return null;
                    }
                });
                clients.add(configService);
            }

            Thread.sleep(30000);

            boolean isPublishOk = clients.get(0).publishConfig(dataId, group, UUID.randomUUID().toString());
            setStartTime();
            System.out.println(isPublishOk);

            String content = clients.get(1).getConfig(dataId, group, 5000);
            System.out.println(content);
            System.in.read();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
