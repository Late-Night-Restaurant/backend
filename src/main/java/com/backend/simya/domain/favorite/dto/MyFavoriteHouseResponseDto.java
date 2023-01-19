package com.backend.simya.domain.favorite.dto;

import com.backend.simya.domain.favorite.entity.Favorite;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
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
public class MyFavoriteHouseResponseDto {

    private Long favoriteId;
    private HouseResponseDto favoriteHouse;

    public static MyFavoriteHouseResponseDto from(Favorite favorite) {
        return MyFavoriteHouseResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .favoriteHouse(HouseResponseDto.from(favorite.getHouse()))
                .build();
    }
}
