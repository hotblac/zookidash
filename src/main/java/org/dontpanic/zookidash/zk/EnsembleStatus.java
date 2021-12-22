package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EnsembleStatus {

    private final ZooKeeperApiClient zkApi;
    private final ZooKeeperCommandClient zkCommand;

    public EnsembleStatus(ZooKeeperApiClient zkApi, ZooKeeperCommandClient zkCommand) {
        this.zkApi = zkApi;
        this.zkCommand = zkCommand;
    }

    public List<Peer> checkStatus(String connectionString) throws IOException, InterruptedException, KeeperException {
        ZooKeeper conn = zkApi.connect(connectionString);
        // TODO exception check

        List<Peer> peers = zkApi.getConfig(conn);
        for (Peer peer : peers) {
            boolean ok = zkCommand.ruok(peer.getPeerHost(), 8080); // TODO work out this port from ZK config?
            peer.setStatus(ok ? Peer.Status.OK : Peer.Status.UNREACHABLE);
        }
        return peers;
    }
}
