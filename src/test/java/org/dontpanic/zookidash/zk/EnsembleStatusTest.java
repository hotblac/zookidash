package org.dontpanic.zookidash.zk;

import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnsembleStatusTest {

    private static final String CONNECT_STRING = "example.com:2181";
    private static final Peer PEER_1 = Peer.builder().id(1).build();
    private static final Peer PEER_2 = Peer.builder().id(2).build();

    @Mock private ZooKeeper conn;
    @Mock private ZooKeeperClient zk;
    @InjectMocks private EnsembleStatus ensembleStatus;

    @Test
    void checkStatus_returnsPeerConfig() throws Exception {
        when(zk.connect(CONNECT_STRING)).thenReturn(conn);
        when(zk.getConfig(conn)).thenReturn(List.of(PEER_1, PEER_2));

        List<Peer> peerConfig = ensembleStatus.checkStatus(CONNECT_STRING);
        assertThat(peerConfig, contains(PEER_1, PEER_2));
    }
}