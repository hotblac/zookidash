package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
class ConfigStringParser {

    List<Peer> parseConfig(String config) {
        return config.lines()
                .map(this::parseLine)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<Peer> parseLine(String line) {
        // Make some assumptions about the format of the config line
        // Assume it's of format server.id=peerHost:quorumPort:electionPort:learnerType;clientHost:clientPort
        // Variations on this format exist but we don't handle them
        if (line.startsWith("server.")) {
            try {
                String[] serverAndConfig = StringUtils.split(line, "=");
                if (serverAndConfig.length != 2) {
                    throw new ConfigParseException();
                }
                String server = serverAndConfig[0];
                String config = serverAndConfig[1];

                String idString = StringUtils.removeStart(server, "server.");
                int id = Integer.parseInt(idString);

                String[] quorumAndClient = StringUtils.split(config, ";");
                if (quorumAndClient.length != 2) {
                    throw new ConfigParseException();
                }
                String quorum = quorumAndClient[0];
                String client = quorumAndClient[1];

                String[] quorumTokens = StringUtils.split(quorum, ":");
                if (quorumTokens.length != 4) {
                    throw new ConfigParseException();
                }
                String peerHost = quorumTokens[0];
                int quorumPort = Integer.parseInt(quorumTokens[1]);
                int electionPort = Integer.parseInt(quorumTokens[2]);
                Peer.LearnerType learnerType = learnerType(quorumTokens[3]);

                String[] clientTokens = StringUtils.split(client, ":");
                String clientHost = clientTokens[0];
                int clientPort = Integer.parseInt(clientTokens[1]);

                return Optional.of(Peer.builder()
                        .id(id)
                        .peerHost(peerHost)
                        .quorumPort(quorumPort)
                        .electionPort(electionPort)
                        .learnerType(learnerType)
                        .clientHost(clientHost)
                        .clientPort(clientPort).build());


            } catch (ConfigParseException | NumberFormatException e) {
                log.warn("Failed to parse line " + line, e);
            }
        }
        return Optional.empty();
    }

    static Peer.LearnerType learnerType(String s) throws ConfigParseException {
        try {
            return Peer.LearnerType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ConfigParseException();
        }
    }

}
