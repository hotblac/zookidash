package org.dontpanic.zookidash.web;

import lombok.extern.slf4j.Slf4j;
import org.dontpanic.zookidash.zk.Peer;
import org.dontpanic.zookidash.zk.EnsembleStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class LandingPageController {

    private final EnsembleStatus ensembleStatus;

    public LandingPageController(EnsembleStatus ensembleStatus) {
        this.ensembleStatus = ensembleStatus;
    }

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute("connectionStringForm", new ConnectionStringForm());
        return "index";
    }

    @PostMapping("/")
    public String landingPage(Model model, @ModelAttribute ConnectionStringForm connectionStringForm) throws Exception {
        List<Peer> peers = ensembleStatus.checkStatus(connectionStringForm.getConnectionString()); // TODO: validate
        log.debug("***SL peers: {}", peers);

        model.addAttribute("peers", peers);
        return "peers";
    }

}
