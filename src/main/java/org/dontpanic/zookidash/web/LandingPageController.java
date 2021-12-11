package org.dontpanic.zookidash.web;

import org.dontpanic.zookidash.zk.ZooKeeperClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LandingPageController {

    private final ZooKeeperClient zk;

    public LandingPageController(ZooKeeperClient zk) {
        this.zk = zk;
    }

    @RequestMapping("/")
    public String landingPage() throws Exception {
        zk.connect("localhost:2181");
        return "index";
    }

}
