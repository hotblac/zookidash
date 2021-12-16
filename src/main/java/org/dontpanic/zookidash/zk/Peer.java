package org.dontpanic.zookidash.zk;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@Builder
public class Peer {
    int id;
    String peerHost;
    int quorumPort;
    int electionPort;
    LearnerType learnerType;
    String clientHost;
    int clientPort;

    enum LearnerType {
        PARTICIPANT,
        OBSERVER
    }

}
