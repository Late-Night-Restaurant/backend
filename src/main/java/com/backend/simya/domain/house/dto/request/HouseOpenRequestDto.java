package com.backend.simya.domain.house.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseOpenRequestDto {

    private Long houseId;
    private int capacity;
    private TopicRequestDto topic;
}
