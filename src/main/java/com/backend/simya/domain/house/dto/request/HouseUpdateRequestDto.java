package com.backend.simya.domain.house.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseUpdateRequestDto {
    private Long houseId;
    private String signboardImageUrl;
    private String houseName;
    private String comment;
}
