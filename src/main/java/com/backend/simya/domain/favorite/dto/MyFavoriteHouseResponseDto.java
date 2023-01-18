package com.backend.simya.domain.favorite.dto;

import com.backend.simya.domain.favorite.entity.Favorite;
import com.backend.simya.domain.house.dto.response.HouseIntroductionResponseDto;
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
    private HouseIntroductionResponseDto houseIntroduction;

    public static MyFavoriteHouseResponseDto from(Favorite favorite) {
        return MyFavoriteHouseResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .houseIntroduction(HouseIntroductionResponseDto.from(
                        favorite.getHouse().getProfile(),
                        favorite.getHouse(),
                        favorite.getHouse().getReviewList()))
                .build();
    }
}
