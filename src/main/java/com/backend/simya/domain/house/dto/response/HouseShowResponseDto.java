package com.backend.simya.domain.house.dto.response;

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
public class HouseShowResponseDto {

    private Long houseId;
    private String houseName;
    private String name;
    private String pictureUrl;
    private String comment;
}
