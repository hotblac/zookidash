package org.dontpanic.zookidash.zk;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

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
    @Setter
    @NonFinal
    @Builder.Default
    Status status = Status.UNKNOWN;

    enum LearnerType {
        PARTICIPANT,
        OBSERVER
    }

    enum Status {
        OK,
        UNREACHABLE,
        UNKNOWN
    }

}
