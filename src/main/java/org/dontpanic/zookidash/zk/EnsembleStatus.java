package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EnsembleStatus {

    private final ZooKeeperClient zk;

    public EnsembleStatus(ZooKeeperClient zk) {
        this.zk = zk;
    }

    public List<Peer> checkStatus(String connectionString) throws IOException, InterruptedException, KeeperException {
        ZooKeeper conn = zk.connect(connectionString);
        // TODO exception check

        return zk.getConfig(conn);
    }
}
