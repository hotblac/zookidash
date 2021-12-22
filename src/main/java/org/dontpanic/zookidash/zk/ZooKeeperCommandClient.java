package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
class ZooKeeperCommandClient {

    private final RestTemplate restTemplate;

    ZooKeeperCommandClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    boolean ruok(String host, int port) {
        String uri = String.format("http://%s:%s/commands/ruok", host, port);
        log.debug("{} ruok...", uri);
        try {
            RuokResponse response = restTemplate.getForObject(uri, RuokResponse.class);
            boolean ok = response != null;
            log.debug("{} ruok {}", uri, ok ? "imok" : "fail");
            return ok;
        } catch (RestClientException e) {
            log.debug(uri + " ruok fail", e);
            return false;
        }
    }


}
