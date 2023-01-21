package com.backend.simya.domain.house.dto.response;

import com.backend.simya.domain.house.entity.Topic;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResponseDto {

    private String title;
    private String content;
    private boolean isTodayTopic;

    public static TopicResponseDto from(Topic topic) {
        return TopicResponseDto.builder()
                .title(topic.getTitle())
                .content(topic.getContent())
                .isTodayTopic(topic.isTodayTopic())
                .build();
    }
}
