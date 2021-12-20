package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class ZooKeeperApiClient {

    private final ConfigStringParser configStringParser;

    ZooKeeperApiClient(ConfigStringParser configStringParser) {
        this.configStringParser = configStringParser;
    }

    ZooKeeper connect(String connectString) throws IOException, InterruptedException {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        log.debug("Zookeeper: attempting connection to {}", connectString);
        ZooKeeper zoo = new ZooKeeper(connectString,5000, we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });

        connectedSignal.await();
        log.debug("Zookeeper: connected");
        return zoo;
    }

    List<Peer> getConfig(ZooKeeper zoo) throws InterruptedException, KeeperException {
        byte[] configData = zoo.getConfig(false, new Stat());
        String config = new String(configData);
        log.debug("config: " + config);

        // Parse the config
        return configStringParser.parseConfig(config);
    }

}
