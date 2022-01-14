package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Component
@Profile("!4lw")
@Slf4j
class ZooKeeperCommandClient implements ZooKeeperServerClient {

    private final WebClient webClient;

    ZooKeeperCommandClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Peer.Status ruok(Peer peer) {
        String host = peer.getPeerHost();
        int port = 8080; // TODO work out this port from ZK config?

        String uri = String.format("http://%s:%s/commands/ruok", host, port);
        log.debug("{} ruok...", uri);
        try {
            RuokResponse response = webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(RuokResponse.class)
                    .block();
            boolean ok = response != null;
            log.debug("{} ruok {}", uri, ok ? "imok" : "fail");
            return ok ? Peer.Status.OK : Peer.Status.UNREACHABLE;
        } catch (WebClientException e) {
            log.debug(uri + " ruok fail", e);
            return Peer.Status.UNREACHABLE;
        }
    }


}
