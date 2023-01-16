package com.backend.simya.domain.house.dto.response;

import com.backend.simya.domain.house.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseResponseDto {

    private Long houseId;
    private Long profileId;
    private Category category;
    private String signboardImageUrl;
    private String houseName;
    private String comment;
    private boolean open;

}
