package org.dontpanic.zookidash.web;

import org.apache.zookeeper.ZooKeeper;
import org.dontpanic.zookidash.zk.Peer;
import org.dontpanic.zookidash.zk.ZooKeeperClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LandingPageControllerTest {

    private static final Peer PEER_1 = Peer.builder().id(1).build();
    private static final Peer PEER_2 = Peer.builder().id(2).build();

    @Mock private ZooKeeperClient zk;
    @Mock private ZooKeeper conn;
    @InjectMocks private LandingPageController controller;

    @Test
    void start_showsConnectionStringForm() {
        ExtendedModelMap model = new ExtendedModelMap();
        String view = controller.start(model);

        assertEquals("index", view);
        assertThat(model.get("connectionStringForm"), isA(ConnectionStringForm.class));
    }

    @Test
    void landingPage_showsPeers() throws Exception {
        ConnectionStringForm form = new ConnectionStringForm();
        form.setConnectionString("example.com:2181");
        when(zk.connect("example.com:2181")).thenReturn(conn);
        when(zk.getConfig(conn)).thenReturn(List.of(PEER_1, PEER_2));

        ExtendedModelMap model = new ExtendedModelMap();
        String view = controller.landingPage(model, form);

        assertEquals("peers", view);
        assertThat((List<Peer>) model.get("peers"), contains(PEER_1, PEER_2));
    }
}