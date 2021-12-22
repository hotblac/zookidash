package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EnsembleStatus {

    private final ZooKeeperApiClient zkApi;
    private final ZooKeeperServerClient zkServer;

    public EnsembleStatus(ZooKeeperApiClient zkApi, ZooKeeperServerClient zkServer) {
        this.zkApi = zkApi;
        this.zkServer = zkServer;
    }

    public List<Peer> checkStatus(String connectionString) throws IOException, InterruptedException, KeeperException {
        ZooKeeper conn = zkApi.connect(connectionString);
        // TODO exception check

        List<Peer> peers = zkApi.getConfig(conn);
        for (Peer peer : peers) {
            Peer.Status status = zkServer.ruok(peer);
            peer.setStatus(status);
        }
        return peers;
    }
}
