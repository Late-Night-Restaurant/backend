package com.backend.simya.domain.chat.controller;

import com.backend.simya.domain.chat.dto.ChatMessage;
import com.backend.simya.domain.chat.dto.ChatMessageCustom;
import com.backend.simya.domain.chat.repository.ChatRoomRepository;
import com.backend.simya.domain.chat.service.ChatService;
import com.backend.simya.domain.jwt.service.TokenProvider;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


/**
 * 서버에서 보내는 입장, 퇴장 안내 메시지 이외의 대화 메시지를 처리하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Controller
//@Controller
public class ChatController {

    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final TokenProvider tokenProvider;

    /**
     * WebSocket "/pub/chat/message" 로 들어오는 메시징 처리
     */
    @MessageMapping("/simya/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {

        try {
            log.info("Web Socket Header 에서 읽어온 Access-Token: {}", token);
            String authenticateUser = tokenProvider.getAuthentication(token).getName();
            String profile = "X";

            try {
                log.info("ChatController - Authenticate User : {}", authenticateUser);
                profile = userService.getSessionToMainProfile(authenticateUser).getNickname();
            } catch (LazyInitializationException | BaseException e) {
                log.error("유저와 대표 프로필 조회에 실패했습니다.");
            }
            log.info("@MessageMapping - authenticateUser: {} => nickname: {}", authenticateUser, profile);

            message.setSender(profile);         // 로그인 회원 정보로 대화명 설정
            message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));  // 채팅방 인원 수 세팅

            // WebSocket 에 발행된 메시지를 Redis 로 발행(publish)s
            chatService.sendChatMessage(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @MessageMapping("/simya/chat/androidMessage")
    public void message(ChatMessageCustom message) {

        try {
            log.info("Web Socket Header 에서 읽어온 Access-Token: {}", message.getToken());
            String authenticateUser = tokenProvider.getAuthentication(message.getToken()).getName();
            log.info("Android에서 가져온 message : {}", message);

            try {
                log.info("ChatController - Authenticate User : {}", authenticateUser);
                Profile profile = userService.getSessionToMainProfile(authenticateUser);
                message.setProfileId(profile.getProfileId());
                message.setSender(profile.getNickname());
                message.setPictureUrl(profile.getPictureUrl());
                message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));  // 채팅방 인원 수 세팅
                log.info("@MessageMapping - authenticateUser: {} => nickname: {}", authenticateUser, profile.getNickname());
            } catch (LazyInitializationException | BaseException e) {
                log.error("유저와 대표 프로필 조회에 실패했습니다.");
            }
            // WebSocket 에 발행된 메시지를 Redis 로 발행(publish)s
            log.info("Android에서 가져온 뒤 바꾼 message : {}", message);
            chatService.sendChatMessage(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
