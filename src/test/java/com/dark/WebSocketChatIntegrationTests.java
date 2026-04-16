package com.dark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dark.model.ChatMessage;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketChatIntegrationTests {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private StompSession session;

    @AfterEach
    void tearDown() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    @Test
    void shouldBroadcastMessageToTargetRoom() throws Exception {
        session = connect();

        String roomId = "room-101";
        BlockingQueue<String> received = new LinkedBlockingQueue<>();
        session.subscribe("/topic/chat/" + roomId, jsonFrameHandler(received));

        // Allow broker time to register the subscription before sending.
        Thread.sleep(200);

        ChatMessage outgoing = new ChatMessage(roomId, "alice", "hello websocket");
        sendAsJson(outgoing);

        String payload = received.poll(5, TimeUnit.SECONDS);
        ChatMessage incoming = payload == null ? null : objectMapper.readValue(payload, ChatMessage.class);
        assertNotNull(incoming, "Expected a message on the subscribed room topic");
        assertEquals(roomId, incoming.getRoomId());
        assertEquals("alice", incoming.getSender());
        assertEquals("hello websocket", incoming.getContent());
    }

    @Test
    void shouldNotDeliverMessageToOtherRooms() throws Exception {
        session = connect();

        String roomA = "room-A";
        String roomB = "room-B";
        BlockingQueue<String> roomAReceived = new LinkedBlockingQueue<>();
        BlockingQueue<String> roomBReceived = new LinkedBlockingQueue<>();

        session.subscribe("/topic/chat/" + roomA, jsonFrameHandler(roomAReceived));
        session.subscribe("/topic/chat/" + roomB, jsonFrameHandler(roomBReceived));

        // Allow broker time to register both subscriptions before sending.
        Thread.sleep(200);

        sendAsJson(new ChatMessage(roomA, "bob", "room A only"));

        String roomAPayload = roomAReceived.poll(5, TimeUnit.SECONDS);
        String roomBPayload = roomBReceived.poll(1, TimeUnit.SECONDS);
        ChatMessage roomAMessage = roomAPayload == null ? null : objectMapper.readValue(roomAPayload, ChatMessage.class);

        assertNotNull(roomAMessage, "Expected room A subscriber to receive the message");
        assertEquals(roomA, roomAMessage.getRoomId());
        assertNull(roomBPayload, "Room B subscriber must not receive room A message");
    }

    private StompSession connect() throws Exception {
        WebSocketStompClient stompClient = createStompClient();
        String url = "http://localhost:" + port + "/ws";

        CompletableFuture<StompSession> future = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {
            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                    byte[] payload, Throwable exception) {
                throw new RuntimeException("STOMP error", exception);
            }
        });

        return future.get(Duration.ofSeconds(10).toMillis(), TimeUnit.MILLISECONDS);
    }

    private WebSocketStompClient createStompClient() {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);
        return new WebSocketStompClient(sockJsClient);
    }

    private StompFrameHandler jsonFrameHandler(BlockingQueue<String> queue) {
        return new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof byte[] bytes) {
                    queue.offer(new String(bytes, StandardCharsets.UTF_8));
                    return;
                }
                queue.offer(String.valueOf(payload));
            }
        };
    }

    private void sendAsJson(ChatMessage message) throws Exception {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("/app/chat/send");
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        session.send(headers, objectMapper.writeValueAsBytes(message));
    }
}