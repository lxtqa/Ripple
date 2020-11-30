package ripple.server.simulation.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fuxiao.tz
 */
@RestController("homeController_api")
public class HomeController {
    @RequestMapping("/Api")
    public String index() {
        return "Ripple Agent";
    }
}
