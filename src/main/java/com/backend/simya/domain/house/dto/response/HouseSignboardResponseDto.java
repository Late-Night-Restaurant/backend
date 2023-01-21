package com.backend.simya.domain.house.dto.response;

import com.backend.simya.domain.house.entity.Category;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.entity.Topic;
import com.backend.simya.domain.review.entity.Review;
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
public class HouseSignboardResponseDto {

    private Long houseId;
    private String category;
    private String signboardImageUrl;
    private String houseName;
    private String todayTopicTitle;

    public static HouseSignboardResponseDto from(House house, String todayTopicTitle) {
        return HouseSignboardResponseDto.builder()
                .houseId(house.getHouseId())
                .category(house.getCategory().getName())
                .signboardImageUrl(house.getSignboardImageUrl())
                .houseName(house.getHouseName())
                .todayTopicTitle(todayTopicTitle)
                .build();
    }
}
