package org.dontpanic.zookidash.web;

import org.dontpanic.zookidash.zk.ZooKeeperClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LandingPageController {

    private final ZooKeeperClient zk;

    public LandingPageController(ZooKeeperClient zk) {
        this.zk = zk;
    }

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute("connectionStringForm", new ConnectionStringForm());
        return "index";
    }

    @PostMapping("/")
    public String landingPage(Model model, @ModelAttribute ConnectionStringForm connectionStringForm) throws Exception {
        zk.connect(connectionStringForm.getConnectionString()); // TODO: validate
        // TODO exception check
        model.addAttribute("isConnected", true);
        return "index";
    }

}
