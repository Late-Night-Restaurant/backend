package com.backend.simya.domain.house.service;

import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.repository.TopicRepository;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.simya.global.common.BaseResponseStatus.FAILED_TO_CREATE_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public void createTopic(House house, HouseOpenRequestDto houseOpenRequestDto) throws BaseException {
        try {
            Topic topic = Topic.builder()
                    .title(houseOpenRequestDto.getTitle())
                    .content(houseOpenRequestDto.getContent())
                    .house(house)
                    .isTodayTopic(false)
                    .activated(true)
                    .build();
            log.info("{} 메뉴가 생성되었습니다.", houseOpenRequestDto.getContent());
            topicRepository.save(topic);

        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CREATE_TOPIC);
        }
    }

}
