package com.backend.simya.domain.house.service;


import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseShowResponseDto;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.repository.HouseRepository;
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
public class HouseService {

    private final HouseRepository houseRepository;
    private final ProfileService profileService;
    private final TopicService topicService;

    @Transactional
    public HouseResponseDto createHouse(HouseRequestDto houseRequestDto) throws BaseException {
        try {
            Optional<Profile> profile = profileService.findProfile(houseRequestDto.getProfileId());
            House savedHouse = houseRepository.save(houseRequestDto.toEntity(profile.get()));

            return HouseResponseDto.builder()
                    .houseId(savedHouse.getHouseId())
                    .category(savedHouse.getCategory())
                    .houseName(savedHouse.getHouseName())
                    .comment(savedHouse.getComment())
                    .signboardImageUrl(savedHouse.getSignboardImageUrl())
                    .build();
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void openHouse(Long houseId, HouseOpenRequestDto houseOpenRequestDto) throws BaseException {
        try {
            House house = getHouse(houseId);
            if (!houseOpenRequestDto.getUserId().equals(house.getProfile().getProfileId())) {  // 이야기 집 생성자가 오픈하려는지 확인
                throw new BaseException(FAILED_TO_OPEN);
            }
            if (house.isOpen()) {  // 이미 오픈된 이야기 집인 경우
                throw new BaseException(HOUSE_ALREADY_OPENED);
            }
            house.openHouse();

            topicService.createTopic(house, houseOpenRequestDto);
            log.info("{} 이야기 집이 오픈되었습니다.", house.getHouseName());
        } catch (Exception ignored) {
            throw new BaseException(HOUSE_OPEN_FAILED);
        }

    }

    public House getHouse(Long houseId) throws BaseException {
        return houseRepository.findById(houseId).orElseThrow(
                () -> new BaseException(HOUSE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public HouseShowResponseDto showHouse(Long houseId) throws BaseException {
        try {
            House house = getHouse(houseId);
            Profile profileInfo = profileService.getProfileInfo(house.getProfile().getProfileId());
            return HouseShowResponseDto.builder()
                    .houseId(house.getHouseId())
                    .houseName(house.getHouseName())
                    .name(profileInfo.getNickname())
                    .picture(profileInfo.getPicture())
                    .comment(profileInfo.getComment())
                    .build();
        } catch (Exception ignored) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void updateMain(Long houseId, HouseUpdateRequestDto houseUpdateRequestDto) throws BaseException {
        try {
            House house = getHouse(houseId);
            if (!houseUpdateRequestDto.getUserId().equals(house.getProfile().getProfileId())) {  // 이야기 집 생성자가 수정하려는지 확인
                throw new BaseException(FAILED_TO_UPDATE);
            }
            house.update(houseUpdateRequestDto);
            log.info("{}의 이야기 집 간판이 수정되었습니다.", house.getHouseName());

        } catch (Exception ignored) {
            throw new BaseException(HOUSE_UPDATE_FAILED);
        }
    }

    @Transactional
    public void closeHouseRoom(Long houseId) throws BaseException {
        try {
            House house = getHouse(houseId);
            house.delete();
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_OPEN_HOUSE);
        }

    }


}
