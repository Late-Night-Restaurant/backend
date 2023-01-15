package com.backend.simya.domain.chattingroom.service;


import com.backend.simya.domain.chattingroom.dto.request.ChattingOpenRequestDto;
import com.backend.simya.domain.chattingroom.dto.request.ChattingRequestDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingResponseDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingShowResponseDto;
import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import com.backend.simya.domain.chattingroom.repository.ChattingRoomRepository;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.service.ProfileService;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ProfileService profileService;
    private final TopicService topicService;

    @Transactional
    public ChattingResponseDto createChattingRoom(ChattingRequestDto chattingRoomRequestDto) throws BaseException {

        try {
            Optional<Profile> profile = profileService.findProfile(chattingRoomRequestDto.getProfileId());
            ChattingRoom chattingRoom = chattingRoomRequestDto.toEntity(profile.get());

            Long chattingRoomId = chattingRoomRepository.save(chattingRoom).getChattingRoomId();
            return new ChattingResponseDto(chattingRoomId, profile.get().getProfileId(), chattingRoom.getCategory(), chattingRoom.getSignboardImageUrl(), chattingRoom.getChattingRoomName(), chattingRoom.getComment(), false);
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void openChattingRoom(Long chattingRoomId, ChattingOpenRequestDto chattingOpenRequestDto) throws BaseException {
        try {
            ChattingRoom chattingRoom = getChattingRoom(chattingRoomId);
            if (!chattingOpenRequestDto.getUserId().equals(chattingRoom.getProfile().getProfileId())) {  // 이야기 집 생성자가 오픈하려는지 확인
                throw new BaseException(FAILED_TO_OPEN);
            }
            if (chattingRoom.isOpen()) {  // 이미 오픈된 이야기 집인 경우
                throw new BaseException(CHATTING_ROOM_ALREADY_OPENED);
            }
            chattingRoom.openChatting();

            topicService.createTopic(chattingRoom, chattingOpenRequestDto);
            log.info("{} 이야기 집이 오픈되었습니다.", chattingRoom.getChattingRoomName());
        } catch (Exception ignored) {
            throw new BaseException(CHATTING_ROOM_OPEN_FAILED);
        }

    }

    public ChattingRoom getChattingRoom(Long chattingRoomId) throws BaseException {
        return chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new BaseException(CHATTING_ROOM_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public ChattingShowResponseDto showChattingRoom(Long chattingRoomId) throws BaseException {
        try {
            ChattingRoom chattingRoom = getChattingRoom(chattingRoomId);
            Profile profileInfo = profileService.getProfileInfo(chattingRoom.getProfile().getProfileId());

            return ChattingShowResponseDto.builder()
                    .chattingId(chattingRoom.getChattingRoomId())
                    .chattingRoomName(chattingRoom.getChattingRoomName())
                    .name(profileInfo.getNickname())
                    .picture(profileInfo.getPicture())
                    .comment(profileInfo.getComment())
                    .build();
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
