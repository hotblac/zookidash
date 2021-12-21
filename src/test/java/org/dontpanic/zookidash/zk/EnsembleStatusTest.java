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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnsembleStatusTest {

    private static final String CONNECT_STRING = "example.com:2181";
    private static final Peer PEER_1 = Peer.builder().id(1).peerHost("host1").clientPort(2181).build();
    private static final Peer PEER_2 = Peer.builder().id(2).peerHost("host2").clientPort(2181).build();

    @Mock private ZooKeeper conn;
    @Mock private ZooKeeperApiClient zkApi;
    @Mock private Zookeeper4lwClient zk4lw;
    @InjectMocks private EnsembleStatus ensembleStatus;

    @Test
    void checkStatus_returnsPeerConfig() throws Exception {
        when(zkApi.connect(CONNECT_STRING)).thenReturn(conn);
        when(zkApi.getConfig(conn)).thenReturn(List.of(PEER_1, PEER_2));

        List<Peer> peerConfig = ensembleStatus.checkStatus(CONNECT_STRING);
        assertThat(peerConfig, contains(PEER_1, PEER_2));
    }

    @Test
    void checkStatus_returnsPeerStatus() throws Exception {
        when(zkApi.connect(CONNECT_STRING)).thenReturn(conn);
        when(zkApi.getConfig(conn)).thenReturn(List.of(PEER_1, PEER_2));
        when(zk4lw.ruok("host1", 2181)).thenReturn(Peer.Status.OK);
        when(zk4lw.ruok("host2", 2181)).thenReturn(Peer.Status.UNREACHABLE);

        List<Peer> peerConfig = ensembleStatus.checkStatus(CONNECT_STRING);

        Peer host1 = peerConfig.stream().filter(peer -> "host1".equals(peer.getPeerHost())).findAny().orElseThrow();
        Peer host2 = peerConfig.stream().filter(peer -> "host2".equals(peer.getPeerHost())).findAny().orElseThrow();
        assertEquals(Peer.Status.OK, host1.getStatus());
        assertEquals(Peer.Status.UNREACHABLE, host2.getStatus());
    }
}