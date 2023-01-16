package com.backend.simya.domain.house.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseOpenRequestDto {

    private Long houseId;
    private Long userId;
    private int capacity;
    private String title;
    private String content;

}
