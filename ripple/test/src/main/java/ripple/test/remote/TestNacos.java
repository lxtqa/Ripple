package ripple.test.remote;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import ripple.test.tools.PayloadGenerator;
import ripple.test.tools.WorkloadGenerator;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author Zhen Tang
 */
public class TestNacos {

    private static final String LIMIT_TIME_PROPERTY = "limitTime";

    private static final String[] CLUSTER_VM_LOCAL = {
            "192.168.2.21"
            , "192.168.2.22"
            , "192.168.2.23"};
    private static final String[] CLUSTER_LAB = {
            "133.133.135.154"
            , "133.133.135.155"
            , "133.133.135.156"
            , "133.133.135.157"};
    private static final String[] CLUSTER_VM_LAB = {
            "192.168.2.11"
            , "192.168.2.12"
            , "192.168.2.13"
            , "192.168.2.14"
            , "192.168.2.15"
            , "192.168.2.16"
            , "192.168.2.17"
            , "192.168.2.18"
            , "192.168.2.19"
            , "192.168.2.20"
            , "192.168.2.21"
            , "192.168.2.22"
            , "192.168.2.23"
            , "192.168.2.24"
            , "192.168.2.25"
            , "192.168.2.26"};

    public static void main(String[] args) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            String dataId = "testApp";
            String group = "test";

            // Nacos限流，默认每秒5次
            System.setProperty(LIMIT_TIME_PROPERTY, String.valueOf(Integer.MAX_VALUE));

            int totalClientCount = 100;
            int clusterSize = 4;
            List<ConfigService> clients = new ArrayList<>();

            int i = 0;
            for (i = 0; i < totalClientCount; i++) {
                String address = CLUSTER_VM_LAB[i % clusterSize];
                ConfigService configService = NacosFactory.createConfigService(address);
                long startTime = System.nanoTime();
                configService.addListener(dataId, group, new Listener() {
                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        // For logging
                        boolean loadTestEnabled = true;
                        if (loadTestEnabled) {
                            long endTime = System.currentTimeMillis();
                            long startTime = Long.parseLong(configInfo.substring(0, configInfo.indexOf(" ")));
                            System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                                    + "] Received: " + (endTime - startTime) + "ms. From DISPATCH.");
                        }
                    }
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }
                });
                long endTime = System.nanoTime();
                System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                        + "] Subscribed: " + (endTime - startTime + 0.00) / 1000 / 1000 + "ms");
                clients.add(configService);
            }

            System.out.println("Subscribe: Done.");

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            WorkloadGenerator.runNacosLoadTest(10, 10, 1024, 100, clients);
            scanner.nextLine();

            for (i = 0; i < totalClientCount; i++) {
                clients.get(i).shutDown();
            }
            System.out.println("Done.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
