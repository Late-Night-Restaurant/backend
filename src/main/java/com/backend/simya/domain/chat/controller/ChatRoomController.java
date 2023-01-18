package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 웹 소켓 통신을 구현한 채팅 화면 View 구성
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 모든 채팅방 리스트 조회
     */
    @GetMapping("/chat/rooms")
    public String rooms(Model model) {
        log.info("# All Chat Rooms");

        model.addAttribute("list", chatRoomRepository.findAllRoom());

        return "chatrooms";
    }

    /*@PostMapping("/chat/rooms")
    public*/

    /**
     * 채팅방 개설
     */
    @PostMapping("/chat/room")
    public String createRoom(@RequestParam String name, RedirectAttributes rttr) {
        log.info("# Create Chat Room, name: {}", name);

        rttr.addAttribute("roomName", chatRoomRepository.createChatRoom(name));
        return "redirect:/chat/rooms";
    }

    /**
     * 채팅방 입장 화면
     */
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "roomdetail";
    }

    /**
     * 툭정 채팅방 조회
     */
    @GetMapping("/room")
    public void roomInfo(String roomId, Model model) {
        log.info("# Get Chat Room, roomId: {}", roomId);
        model.addAttribute("room", chatRoomRepository.findRoomById(roomId));
    }
}
