package com.example.chat.config.websocket;

import com.example.chat.chatting.domain.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    // 소켓 세션을 저장할 set
    private final Set<WebSocketSession> sessions = new HashSet<>();

    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨.", session.getId());
        sessions.add(session);
        //ChatMessage chatMessage = new ChatMessage(session.getId(), "websocket 연결 완료");
        //session.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
    }

    // 메세지 수신시
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessage chatMessage = mapper.readValue(payload, ChatMessage.class);
        log.info("session {}", chatMessage.toString());

        if (!session.isOpen()) {
            log.info("session 닫힘.");
            chatMessage.updateSender(null);
            chatMessage.updateMessage("이미 닫힌 채팅방입니다.");
            for (WebSocketSession webSocketSession : chatRoomSessionMap.get(chatMessage.getRoomId())) {
                webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
            }
            return;
        }

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            chatRoomSessionMap.computeIfAbsent(chatMessage.getRoomId(), s -> new HashSet<>()).add(session);

            log.info("session 개수 : {}", chatRoomSessionMap.get(chatMessage.getRoomId()).size());

            chatMessage.updateMessage(chatMessage.getSender() + "님이 입장했습니다.");
        } else if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            chatRoomSessionMap.get(chatMessage.getRoomId()).remove(session);

            log.info("session 개수 : {}", chatRoomSessionMap.get(chatMessage.getRoomId()).size());

            chatMessage.updateMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
        }

        for (WebSocketSession webSocketSession : chatRoomSessionMap.get(chatMessage.getRoomId())) {
            webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
        }
    }

    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);       // 만든사람이 나가면 사라짐.
        ChatMessage chatMessage = new ChatMessage(session.getId(), "websocket 연결 종료.");
        session.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
    }
}
