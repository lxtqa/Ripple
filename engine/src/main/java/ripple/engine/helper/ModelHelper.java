package ripple.engine.helper;

import ripple.engine.api.model.AgentModel;
import ripple.engine.api.model.MessageModel;
import ripple.engine.api.model.NodeModel;
import ripple.engine.api.model.TestDetailModel;
import ripple.engine.api.model.TestSummaryModel;
import ripple.engine.entity.Agent;
import ripple.engine.entity.Message;
import ripple.engine.entity.Node;
import ripple.engine.entity.TestDetail;
import ripple.engine.entity.TestSummary;
import org.springframework.stereotype.Component;

/**
 * @author fuxiao.tz
 */
@Component
public class ModelHelper {
    public AgentModel parseAgent(Agent agent) {
        AgentModel agentModel = new AgentModel();
        agentModel.setUuid(agent.getUuid());
        agentModel.setAddress(agent.getAddress());
        agentModel.setPort(agent.getPort());
        return agentModel;
    }

    public NodeModel parseNode(Node node) {
        NodeModel nodeModel = new NodeModel();
        nodeModel.setUuid(node.getUuid());
        nodeModel.setAgentUuid(node.getAgentUuid());
        nodeModel.setClusterUuid(node.getClusterUuid());
        nodeModel.setType(node.getType());
        nodeModel.setAddress(node.getAddress());
        nodeModel.setPort(node.getPort());
        return nodeModel;
    }

    public MessageModel parseMessage(Message message) {
        MessageModel messageModel = new MessageModel();
        messageModel.setClusterUuid(message.getClusterUuid());
        messageModel.setContent(message.getContent());
        messageModel.setDate(message.getDate());
        messageModel.setMessageUuid(message.getMessageUuid());
        messageModel.setNodeUuid(message.getNodeUuid());
        return messageModel;
    }

    public TestSummaryModel parseTestSummary(TestSummary testSummary) {
        TestSummaryModel testSummaryModel = new TestSummaryModel();
        testSummaryModel.setNodeType(testSummary.getNodeType());
        testSummaryModel.setNodeCount(testSummary.getNodeCount());
        testSummaryModel.setTps(testSummary.getTps());
        testSummaryModel.setDuration(testSummary.getDuration());
        testSummaryModel.setMinTime(testSummary.getMinTime());
        testSummaryModel.setMaxTime(testSummary.getMaxTime());
        for (TestDetail testDetail : testSummary.getDetails().values()) {
            testSummaryModel.getDetails().add(this.parseTestDetail(testDetail));
        }
        return testSummaryModel;
    }

    public TestDetailModel parseTestDetail(TestDetail testDetail) {
        TestDetailModel testDetailModel = new TestDetailModel();
        testDetailModel.setMessageUuid(testDetail.getMessageUuid());
        testDetailModel.setStartDate(testDetail.getStartDate());
        testDetailModel.setEndDate(testDetail.getEndDate());
        testDetailModel.setRunTime(testDetail.getRunTime());
        return testDetailModel;
    }
}
