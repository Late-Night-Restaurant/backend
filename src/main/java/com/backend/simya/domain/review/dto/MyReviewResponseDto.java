package com.backend.simya.domain.review.dto;

import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.entity.House;
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
public class MyReviewResponseDto {

    private HouseResponseDto reviewedHouse;
    private ReviewResponseDto myReview;

    public static MyReviewResponseDto from(House house, Review review) {
        return MyReviewResponseDto.builder()
                .reviewedHouse(HouseResponseDto.from(house))
                .myReview(ReviewResponseDto.from(review))
                .build();
    }
}
