package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatRoom;
import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import com.backend.simya.domain.jwt.service.AuthService;
import com.backend.simya.domain.jwt.service.TokenProvider;
import com.backend.simya.domain.user.dto.response.ChatLoginInfo;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/simya/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/room")
    public String rooms(Model model) {
        log.info("Controller- /room 실행");
        return "/chat/room";
    }

    /**
     * 모든 채팅방 리스트 조회
     * -> 이때 userCount 정보 세팅
     */
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        log.info("# All Chat Rooms");
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom();
        chatRooms.forEach(
                room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId()))
        );
        return chatRooms;
    }

    /**
     * 채팅방 개설
     */
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        log.info("# Create Chat Room, name: {}", name);
        return chatRoomRepository.createChatRoom(name);
    }

    /**
     * 채팅방 입장 화면
     */
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    /**
     * 툭정 채팅방 조회
     */
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        log.info("# Get Chat Room, roomId: {}", roomId);
        return chatRoomRepository.findRoomById(roomId);
    }

    /**
     * 회원 정보 조회
     * // TODO User App 에서 로그인한 사용자 가져오기
     */
    @GetMapping("/user")
    @ResponseBody
    public ChatLoginInfo getUserInfo() {
        log.info("ChatLoginInfo 응답 객체 반환을 위한 준비");
        try {
//            User loginUser = userService.getMyUserWithAuthorities();
            User loginUser = authService.authenticateUser();

            log.info("Authentication - {}", loginUser.getUsername());

            int idx = loginUser.getMainProfile();
            String name = loginUser.getProfileList().get(idx).getNickname();  // 대표 프로필의 닉네임으로 sender 명 지정
            String token = tokenProvider.getJwt().getAccessToken();
            log.info("JWT nickname / Token - " + name + " " + token);
            return ChatLoginInfo.builder()
                    .name(name)
                    .token(token)  // TODO 프론트에서 "token" 키 값을 받아올 때 ChatLoginDto 의 값에서 가져오는 것
                    .build();
        } catch (BaseException e) {
            log.error("ChatLoginInfo 객체 생성 실패");
            return ChatLoginInfo.builder()
                    .name("Bad Request")
                    .token("Invalid Token")
                    .build();
        }
    }

    /**
     * 채팅방 얼리기
     */
    @PatchMapping("/room /{roomId}")
    @ResponseBody
    public ChatRoom freezeRoom(@PathVariable String roomId) {
        return chatRoomRepository.freezeChatRoom(roomId);

    }
}