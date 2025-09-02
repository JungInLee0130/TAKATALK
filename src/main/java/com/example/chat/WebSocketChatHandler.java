package com.example.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨.", session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        log.info("session {}", chatMessageDto.toString());

        String roomId = chatMessageDto.getRoomId();

        if (!chatRoomSessionMap.containsKey(roomId)) {
            chatRoomSessionMap.put(roomId, new HashSet<>());
        }

        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(roomId);

        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
            chatRoomSession.add(session);
            chatMessageDto.updateMessage(chatMessageDto.getSender() + "님이 입장했습니다.");
        } else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.QUIT)) {
            chatRoomSession.remove(session);
            chatMessageDto.updateMessage(chatMessageDto.getSender() + "님이 퇴장했습니다.");
        } else {

        }
        sendMessageToChatRoom(chatMessageDto, chatRoomSession);
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }
}
