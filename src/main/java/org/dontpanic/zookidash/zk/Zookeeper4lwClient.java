package org.dontpanic.zookidash.zk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
@Slf4j
public class Zookeeper4lwClient {

    boolean ruok(String host, int port) {

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            // Issue RUOK command
            out.println("ruok");

            // Wait for response
            // TODO: Fail on timeout
            String response = in.readLine();
            return "imok".equals(response);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
