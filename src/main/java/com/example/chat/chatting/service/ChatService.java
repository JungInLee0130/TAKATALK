package com.example.chat.chatting.service;

import com.example.chat.chatting.domain.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;



    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        /*채팅방세션 살아있는지 확인 -> 살아있으면 go
        * -> 죽었으면 error 처리?*/
        ChatRoom chatRoom = chatRooms.get(roomId);
        return chatRoom;
    }

    public ChatRoom createRoom(String roomname) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(roomname)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
}
