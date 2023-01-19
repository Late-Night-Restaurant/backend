package com.backend.simya.domain.house.dto.request;

import com.backend.simya.domain.house.entity.House;
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
public class TopicRequestDto {

    private String title;
    private String content;

    public Topic toEntity(House house, boolean isMain) {
        return Topic.builder()
                .title(this.getTitle())
                .content(this.getContent())
                .isTodayTopic(isMain)
                .house(house)
                .build();
    }
}
