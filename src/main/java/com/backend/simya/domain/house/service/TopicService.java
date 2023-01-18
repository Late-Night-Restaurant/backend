package com.backend.simya.domain.house.service;

import com.backend.simya.domain.house.dto.request.TopicRequestDto;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.repository.TopicRepository;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.simya.global.common.BaseResponseStatus.FAILED_TO_CREATE_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public Topic createTopic(House houseToRegisterTopic, TopicRequestDto topicToRegisterRequestDto) throws BaseException {
        try {
            Topic newTopic = topicToRegisterRequestDto.toEntity(houseToRegisterTopic);
            return topicRepository.save(newTopic);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CREATE_TOPIC);
        }
    }

    @Transactional(readOnly = true)
    public List<Topic> findAllTopic(Long houseId) {
        return topicRepository.findAllByHouseId(houseId);
    }

    @Transactional
    public void deleteAllTopic(Long houseId) {
        List<Topic> allTopic = findAllTopic(houseId);
        topicRepository.deleteAll(allTopic);
    }

}
