package com.backend.simya.domain.house.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HouseShowResponseDto {
    private Long houseId;
    private String houseName;
    private String name;
    private String picture;
    private String comment;
}
