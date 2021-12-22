package org.dontpanic.zookidash.zk;

interface ZooKeeperServerClient {
    Peer.Status ruok(Peer peer);
}
