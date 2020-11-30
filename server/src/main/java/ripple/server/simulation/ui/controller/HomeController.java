package ripple.server.simulation.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author fuxiao.tz
 */
@Controller("homeController_ui")
public class HomeController {
    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index")
                .addObject("module", "home")
                .addObject("title", "Ripple Agent");
    }
}