package com.backend.simya.domain.house.dto.response;

import com.backend.simya.domain.house.entity.Category;
import com.backend.simya.domain.house.entity.House;
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
public class HouseResponseDto {

    private Long houseId;
    private Category category;
    private String signboardImageUrl;
    private String houseName;
    private String comment;

    public static HouseResponseDto from(House house) {
        return HouseResponseDto.builder()
                .houseId(house.getHouseId())
                .houseName(house.getHouseName())
                .category(house.getCategory())
                .comment(house.getComment())
                .build();
    }
}
