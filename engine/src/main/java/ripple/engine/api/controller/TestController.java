package ripple.engine.api.controller;

import ripple.engine.api.model.TestSummaryModel;
import ripple.engine.entity.Message;
import ripple.engine.entity.TestSummary;
import ripple.engine.helper.ModelHelper;
import ripple.engine.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fuxiao.tz
 */
@RestController
public class TestController {
    private TestService testService;
    private ModelHelper modelHelper;

    private TestService getTestService() {
        return testService;
    }

    @Autowired
    private void setTestService(TestService testService) {
        this.testService = testService;
    }

    private ModelHelper getModelHelper() {
        return modelHelper;
    }

    @Autowired
    private void setModelHelper(ModelHelper modelHelper) {
        this.modelHelper = modelHelper;
    }

    @RequestMapping(value = {"/Api/Test/Start"}, method = {RequestMethod.POST})
    public TestSummaryModel startTest(@RequestHeader("x-ripple-engine-node-type") String nodeType
            , @RequestHeader("x-ripple-engine-node-count") int nodeCount
            , @RequestHeader("x-ripple-engine-tps") int tps
            , @RequestHeader("x-ripple-engine-duration") int duration) {
        List<Message> messageList = this.getTestService().startTest(nodeType, nodeCount, tps, duration);
        TestSummary testSummary = this.getTestService().analyzeMessages(nodeType, nodeCount, tps, duration, messageList);
        return this.getModelHelper().parseTestSummary(testSummary);
    }
}
