package org.dontpanic.zookidash.web;

import org.dontpanic.zookidash.zk.ZooKeeperClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LandingPageController {

    private static final String CONNECTION_STRING_FORM_ATTRIBUTE = "connectionStringForm";

    private final ZooKeeperClient zk;

    public LandingPageController(ZooKeeperClient zk) {
        this.zk = zk;
    }

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute(CONNECTION_STRING_FORM_ATTRIBUTE, new ConnectionStringForm());
        return "index";
    }

    @PostMapping("/")
    public String landingPage(@ModelAttribute(CONNECTION_STRING_FORM_ATTRIBUTE) ConnectionStringForm connectionStringForm) throws Exception {
        zk.connect(connectionStringForm.getConnectionString()); // TODO: validate
        return "index";
    }

}
