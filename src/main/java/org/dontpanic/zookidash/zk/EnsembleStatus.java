package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EnsembleStatus {

    private final ZooKeeperApiClient zkApi;
    private final Zookeeper4lwClient zk4lw;

    public EnsembleStatus(ZooKeeperApiClient zkApi, Zookeeper4lwClient zk4lw) {
        this.zkApi = zkApi;
        this.zk4lw = zk4lw;
    }

    public List<Peer> checkStatus(String connectionString) throws IOException, InterruptedException, KeeperException {
        ZooKeeper conn = zkApi.connect(connectionString);
        // TODO exception check

        List<Peer> peers = zkApi.getConfig(conn);
        for (Peer peer : peers) {
            Peer.Status status = zk4lw.ruok(peer.getPeerHost(), peer.getClientPort());
            peer.setStatus(status);
        }
        return peers;
    }
}
