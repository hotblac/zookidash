package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
public class ZooKeeperClient {

    private final Logger log = LoggerFactory.getLogger(ZooKeeperClient.class);

    public ZooKeeper connect(String connectString) throws IOException, InterruptedException {
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

}
