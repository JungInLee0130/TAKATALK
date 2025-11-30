package com.example.chat.chatting.controller;

import com.example.chat.chatting.domain.ChatRoom;
import com.example.chat.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /*채팅방 목록*/

    @GetMapping("/main")
    public String index(){
        return "chat/chatList";
    }

    @GetMapping("/chatList")
    public String chatList(Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/chatList";
    }

    /*채팅방 생성 -> 채팅방으로 이동*/
    @PostMapping(value = "/createRoom", produces="application/string;charset=UTF-8")
    public String createRoom(Model model, @RequestParam String name, String nickname) {
        ChatRoom room = chatService.createRoom(name);
        model.addAttribute("room", room);
        model.addAttribute("nickname", nickname);
        // redirect://chat/chatList로 해야하긴함.
        // 근데 이럼 세션 유지가 안됨.
        return "chat/chatRoom";
    }

    // 채팅방입장
    @GetMapping(value = "/chatRoom", produces="application/string;charset=UTF-8")
    public String chatRoom(   Model model
                            , @RequestParam String roomId
                            , @RequestParam String nickname) {
        ChatRoom room = chatService.findRoomById(roomId);
        model.addAttribute("room", room);
        model.addAttribute("nickname", nickname);
        return "chat/chatRoom";
    }
}
