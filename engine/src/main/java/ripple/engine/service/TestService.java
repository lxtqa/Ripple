package ripple.engine.service;

import ripple.engine.entity.Cluster;
import ripple.engine.entity.Context;
import ripple.engine.entity.Message;
import ripple.engine.entity.Node;
import ripple.engine.entity.TestDetail;
import ripple.engine.entity.TestSummary;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fuxiao.tz
 */
@Component
public class TestService {
    private Context context;
    private NodeService nodeService;

    private Context getContext() {
        return context;
    }

    @Autowired
    private void setContext(Context context) {
        this.context = context;
    }

    private NodeService getNodeService() {
        return nodeService;
    }

    @Autowired
    private void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public List<Message> startTest(String nodeType, int nodeCount, int tps, int duration) {
        try {
            UUID clusterUuid = UUID.randomUUID();
            this.getNodeService().createCluster(clusterUuid, nodeType, nodeCount);
            Cluster cluster = this.getContext().getClusters().get(clusterUuid);
            List<Node> nodeList = Collections.list(cluster.getNodes().elements());
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("demo-pool-%d").build();
            ExecutorService pool = new ThreadPoolExecutor(50, 50,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE)
                    , namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
            int i;
            int j;
            int nodeId = 0;
            List<Future> taskList = new ArrayList<>();
            for (i = 0; i < duration; i++) {
                for (j = 0; j < tps; j++) {
                    taskList.add(pool.submit(new StarUpdateWork(nodeList.get(nodeId++ % nodeList.size()), UUID.randomUUID())));
                }
                Thread.sleep(1000);
            }
            for (Future future : taskList) {
                future.get();
            }
            pool.shutdown();
            List<Message> messageList = this.getNodeService().gatherMessages(clusterUuid);
            this.getNodeService().removeCluster(clusterUuid);
            return messageList;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public TestSummary analyzeMessages(String nodeType, int nodeCount, int tps, int duration, List<Message> messageList) {
        TestSummary testSummary = new TestSummary(nodeType, nodeCount, tps, duration);
        ConcurrentHashMap<UUID, TestDetail> testDetails = testSummary.getDetails();
        for (Message message : messageList) {
            if (!testDetails.containsKey(message.getMessageUuid())) {
                testDetails.put(message.getMessageUuid(), new TestDetail(message.getMessageUuid()));
            }
            TestDetail testDetail = testDetails.get(message.getMessageUuid());
            if (testDetail.getStartDate() == null) {
                testDetail.setStartDate(message.getDate());
            }
            if (testDetail.getEndDate() == null) {
                testDetail.setEndDate(message.getDate());
            }
            if (testDetail.getStartDate().after(message.getDate())) {
                testDetail.setStartDate(message.getDate());
            }
            if (testDetail.getEndDate().before(message.getDate())) {
                testDetail.setEndDate(message.getDate());
            }
            testDetail.setRunTime(testDetail.getEndDate().getTime() - testDetail.getStartDate().getTime());
        }
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        for (TestDetail testDetail : testDetails.values()) {
            if (testDetail.getRunTime() > maxTime) {
                maxTime = testDetail.getRunTime();
            }
            if (testDetail.getRunTime() < minTime) {
                minTime = testDetail.getRunTime();
            }
        }
        testSummary.setMaxTime(maxTime);
        testSummary.setMinTime(minTime);
        return testSummary;
    }
}
