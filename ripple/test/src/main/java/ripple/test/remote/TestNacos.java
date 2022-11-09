package ripple.test.remote;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Zhen Tang
 */
public class TestNacos {
    private static final List<NodeMetadata> VM_CLUSTER = new ArrayList<>(Arrays.asList(
            new NodeMetadata(1, "192.168.2.21", 8848)
            , new NodeMetadata(2, "192.168.2.21", 8848)
            , new NodeMetadata(3, "192.168.2.21", 8848)));

    public static void main(String[] args) {
        try {
            String serverAddr = "192.168.2.21";
            String dataId = "nacos.cfg.dataId";
            String group = "test";
            ConfigService configService = NacosFactory.createConfigService(serverAddr);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("recieve:" + configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });

            boolean isPublishOk = configService.publishConfig(dataId, group, "content");
            System.out.println(isPublishOk);
            configService.publishConfig(dataId, group, "test456");

            Thread.sleep(3000);
            content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);

//            boolean isRemoveOk = configService.removeConfig(dataId, group);
//            System.out.println(isRemoveOk);
//            Thread.sleep(3000);

            content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            Thread.sleep(300000);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
