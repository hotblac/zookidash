package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
@Profile("4lw")
@Slf4j
class ZooKeeper4lwClient implements ZooKeeperServerClient {

    public Peer.Status ruok(Peer peer) {

        String host = peer.getPeerHost();
        int port = peer.getClientPort();
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            log.debug("{}:{} ruok", host, port);

            // Issue RUOK command
            out.println("ruok");

            // Wait for response
            // TODO: Fail on timeout
            String response = in.readLine();
            log.debug("{}:{} ruok response: {}", host, port, response);

            if ("imok".equals(response)) {
                return Peer.Status.OK;
            } else if (StringUtils.contains(response, "not in the whitelist")) {
                return Peer.Status.QUERY_REFUSED;
            } else {
                return Peer.Status.UNKNOWN;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Peer.Status.UNREACHABLE;
        }
    }

}
