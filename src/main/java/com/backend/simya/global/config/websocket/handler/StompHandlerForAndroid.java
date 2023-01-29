/*
package com.backend.simya.global.config.websocket.handler;

import com.backend.simya.domain.chat.dto.ChatMessageForAndroid;
import com.backend.simya.domain.chat.repository.ChatRoomRepositoryForAndroid;
import com.backend.simya.domain.chat.service.ChatServiceForAndroid;
import com.backend.simya.domain.jwt.service.TokenProvider;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompHandlerForAndroid implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatServiceForAndroid chatService;
    private final UserService userService;

    */
/**
     * WebSocket 을 통해 들어온 요청 처리 전에 수행되는 메소드
     * -> WebSocket 연결 시, 요청 헤더의 토큰 유효성을 검증
     *
     * * 유효하지 않은 JWT 토큰 셋팅 시에는 WebSocket 연결을 하지 않고 에외처리됨
     *//*

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
            if (StompCommand.CONNECT == accessor.getCommand()) {
                connectRoom(accessor);
            } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
                subscribeRoom(message);
            } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
                disconnectRoom(message);
            }
        } catch (BaseException e) {
            log.error(e.getMessage());
        }
        return message;
    }


    private void connectRoom(StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader("token");
        log.info("CONNECT {}", accessToken);
        tokenProvider.validateToken(accessToken);
    }

    private void subscribeRoom(Message<?> message) throws BaseException {
        Long roomId = extractRoomId(message);
        String sessionId = extractSessionId(message);

        // 클라이언트의 입장 메시지 채팅방에 발송 -> 입/퇴장 안내는 서버에서 일괄적으로 처리 : Redis Publish
        String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
        Profile profile = userService.getSessionToMainProfile(name);
        chatService.sendChatMessage(ChatMessageForAndroid.builder()
                .type(ChatMessageForAndroid.MessageType.ENTER)
                .roomId(roomId)
                .senderProfile(ProfileResponseDto.from(profile))
                .build());

        chatService.enterChatRoom(sessionId, roomId);
    }

    private void disconnectRoom(Message<?> message) throws BaseException {
        String sessionId = extractSessionId(message);
        Long roomId = extractRoomId(message);
        //Long roomId = chatRoomRepository.findUserEnterRoomId(sessionId);

        // 클라이언트의 퇴장 메시지 채팅방에 발송 -> 이후 퇴장한 클라이언트의 roomId 매핑 정보 삭제 : Redis Publish
        String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");

        Profile profile = userService.getSessionToMainProfile(name);
        chatService.sendChatMessage(ChatMessageForAndroid.builder()
                .type(ChatMessageForAndroid.MessageType.QUIT)
                .roomId(roomId)
                .senderProfile(ProfileResponseDto.from(profile))
                .build());

        chatService.exitChatRoom(sessionId, roomId);
    }

    private String extractSessionId(Message<?> message) {
        return (String) message
                .getHeaders()
                .get("simpSessionId");
    }

    private Long extractRoomId(Message<?> message) {
        return chatService.getRoomId(Optional.ofNullable(
                (String) message.getHeaders()
                        .get("simpDestination")).orElse("InvalidRoomId"));
    }
}

*/
