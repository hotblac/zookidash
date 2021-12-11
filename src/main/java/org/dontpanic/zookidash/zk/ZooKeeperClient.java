package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
public class ZooKeeperClient {

    public ZooKeeper connect(String connectString) throws IOException, InterruptedException {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zoo = new ZooKeeper(connectString,5000, we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });

        connectedSignal.await();
        return zoo;
    }

}
