package org.dontpanic.zookidash.zk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZooKeeperCommandClientTest {

    private static final String PEER_HOST = "peer.host";
    private static final Peer PEER = Peer.builder().peerHost(PEER_HOST).build();

    private final ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
    private final WebClient webClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
    private final ZooKeeperCommandClient client = new ZooKeeperCommandClient(webClient);

    @Captor private ArgumentCaptor<ClientRequest> requestCaptor;

    @Test
    void ruok_sendsGetRequest() {
        stubOkResponse();

        client.ruok(PEER);

        verify(exchangeFunction).exchange(requestCaptor.capture());
        ClientRequest request = requestCaptor.getValue();
        assertEquals(HttpMethod.GET, request.method());
        assertEquals("http://peer.host:8080/commands/ruok", request.url().toString());
    }

    @Test
    void okResponse_returnsOkStatus() {
        stubOkResponse();
        Peer.Status status = client.ruok(PEER);
        assertEquals(Peer.Status.OK, status);
    }

    @Test
    void noResponse_returnsUnreachableStatus() {
        stubEmptyResponse();
        Peer.Status status = client.ruok(PEER);
        assertEquals(Peer.Status.UNREACHABLE, status);
    }

    @Test
    void errorResponse_returnsUnreachableStatus() throws Exception {
        stubConnectionExceptionResponse();
        Peer.Status status = client.ruok(PEER);
        assertEquals(Peer.Status.UNREACHABLE, status);
    }

    private void stubOkResponse() {
        ClientResponse response = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{ \"command\": \"ruok\" }").build();
        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(response));
    }

    private void stubEmptyResponse() {
        ClientResponse emptyResponse = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(emptyResponse));
    }

    private void stubConnectionExceptionResponse() throws URISyntaxException {
        ConnectException connEx = new ConnectException("Simulated connection exception");
        when(exchangeFunction.exchange(any(ClientRequest.class))).thenThrow(new WebClientRequestException(connEx, HttpMethod.GET, new URI("http://peer.host:8080/commands/ruok"), new HttpHeaders()));
    }

}