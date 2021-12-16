package org.dontpanic.zookidash.zk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

class ConfigStringParserTest {

    private final ConfigStringParser parser = new ConfigStringParser();

    @Test
    void emptyString_zeroServers() {
        List<Peer> servers = parser.parseConfig("");
        assertThat(servers, empty());
    }

    @Test
    void multiLineConfig_multipleServers() {
        List<Peer> servers = parser.parseConfig("server.1=localhost:2780:2783:participant;localhost:2791\n" +
                "server.2=localhost:2781:2784:participant;localhost:2792\n" +
                "server.3=localhost:2782:2785:participant;localhost:2793\n" +
                "version=400000003");
        assertThat(servers, contains(
                Peer.builder().id(1).peerHost("localhost").quorumPort(2780).electionPort(2783).learnerType(Peer.LearnerType.PARTICIPANT).clientHost("localhost").clientPort(2791).build(),
                Peer.builder().id(2).peerHost("localhost").quorumPort(2781).electionPort(2784).learnerType(Peer.LearnerType.PARTICIPANT).clientHost("localhost").clientPort(2792).build(),
                Peer.builder().id(3).peerHost("localhost").quorumPort(2782).electionPort(2785).learnerType(Peer.LearnerType.PARTICIPANT).clientHost("localhost").clientPort(2793).build())
        );
    }

    @Test
    void badLine_isIgnored() {
        List<Peer> servers = parser.parseConfig("server.1=localhost:2780:2783:participant;localhost:2791\n" +
                "server.2=abadline\n" +
                "server.3=localhost:2782:2785:participant;localhost:2793\n" +
                "version=400000003");
        assertThat(servers, contains(
                Peer.builder().id(1).peerHost("localhost").quorumPort(2780).electionPort(2783).learnerType(Peer.LearnerType.PARTICIPANT).clientHost("localhost").clientPort(2791).build(),
                Peer.builder().id(3).peerHost("localhost").quorumPort(2782).electionPort(2785).learnerType(Peer.LearnerType.PARTICIPANT).clientHost("localhost").clientPort(2793).build())
        );
    }
}