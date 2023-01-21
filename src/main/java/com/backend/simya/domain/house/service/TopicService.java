package com.backend.simya.domain.house.service;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.house.repository.TopicRepository;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.simya.global.common.BaseResponseStatus.DATABASE_ERROR;
import static com.backend.simya.global.common.BaseResponseStatus.FAILED_TO_CREATE_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public Topic createTopic(Topic topicToRegister) throws BaseException {
        try {
            log.info("TopicService - topicToRegister {}", topicToRegister.getTitle());
            return topicRepository.save(topicToRegister);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CREATE_TOPIC);
        }
    }

    @Transactional(readOnly = true)
    public List<Topic> findAllTopic(Long houseId) {
        return topicRepository.findAllByHouseId(houseId);
    }

    public Topic findTopic(Long topicId) throws BaseException {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new BaseException(DATABASE_ERROR));
    }
}
