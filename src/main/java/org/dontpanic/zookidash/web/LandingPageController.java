package org.dontpanic.zookidash.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.dontpanic.zookidash.zk.Peer;
import org.dontpanic.zookidash.zk.ZooKeeperClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
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
        // TODO: store conn in the session
        ZooKeeper conn = zk.connect(connectionStringForm.getConnectionString()); // TODO: validate
        // TODO exception check

        List<Peer> peers = zk.getConfig(conn);
        log.debug("***SL peers: {}", peers);

        model.addAttribute("isConnected", true);
        model.addAttribute("peers", peers);
        return "index";
    }

}
