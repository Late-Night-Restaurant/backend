package com.backend.simya.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
//@RestController
@Controller
@RequestMapping("/simya/chat")
public class ChatController {

//    private final ChatService chatService;

    @GetMapping
    public String chatGet() {
        log.info("@ChatController, chat GET()");
        return "chat";
    }
    /*@PostMapping
    public ChatRoom createRoom(@RequestBody String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }*/
}
