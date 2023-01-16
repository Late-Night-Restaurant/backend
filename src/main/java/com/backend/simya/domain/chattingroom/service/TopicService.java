package com.backend.simya.domain.chattingroom.service;

import com.backend.simya.domain.chattingroom.dto.request.ChattingOpenRequestDto;
import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import com.backend.simya.domain.chattingroom.entity.Topic;
import com.backend.simya.domain.chattingroom.repository.TopicRepository;
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
    public void createTopic(ChattingRoom chattingRoom, ChattingOpenRequestDto chattingOpenRequestDto) throws BaseException {
        try {
            Topic topic = Topic.builder()
                    .title(chattingOpenRequestDto.getTitle())
                    .content(chattingOpenRequestDto.getContent())
                    .chattingRoom(chattingRoom)
                    .isTodayTopic(false)
                    .activated(true)
                    .build();
            log.info("{} 메뉴가 생성되었습니다.", chattingOpenRequestDto.getContent());
            topicRepository.save(topic);

        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CREATE_TOPIC);
        }
    }

}
