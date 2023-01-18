/*
package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatRoom;
import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * 웹 소켓 통신을 구현한 채팅 화면 View 구성
 *//*

@RequiredArgsConstructor
@Controller
@RequestMapping("/simya/chat")
public class ChatRoomSubController {

    private final ChatRoomRepository chatRoomRepository;

    */
/**
     * 채팅 리스트 화면
     *//*

    @GetMapping("/room")
    public String rooms(Model model) {
        return "room";
    }

    */
/**
     * 모든 채팅방 목록 반환
     *//*

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    */
/**
     * 채팅방 생성
     *//*

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    */
/**
     * 채팅방 입장 화면
     *//*

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "roomdetail";
    }

    */
/**
     * 툭정 채팅방 조회
     *//*

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
*/
