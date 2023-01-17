package com.backend.simya.domain.chattingroom.service;


import com.backend.simya.domain.chattingroom.dto.request.ChattingRoomRequestDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingRoomResponseDto;
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

import static com.backend.simya.global.common.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ProfileService profileService;

    @Transactional
    public ChattingRoomResponseDto createChattingRoom(ChattingRoomRequestDto chattingRoomRequestDto) throws BaseException {

        try {
            Optional<Profile> profile = profileService.findProfile(chattingRoomRequestDto.getProfileId());
            ChattingRoom chattingRoom = chattingRoomRequestDto.toEntity(profile.get());

            Long chattingRoomId = chattingRoomRepository.save(chattingRoom).getChattingRoomId();
            return new ChattingRoomResponseDto(chattingRoomId, profile.get().getProfileId(), chattingRoom.getCategory(), chattingRoom.getSignboardImageUrl(), chattingRoom.getChattingRoomName(), chattingRoom.getComment());
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
