package com.backend.simya.domain.house.dto.response;


import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HouseIntroductionResponseDto {

    private ProfileResponseDto masterProfile;
    private HouseResponseDto houseInfo;
    private List<ReviewResponseDto> houseReviewList;

    public static HouseIntroductionResponseDto from(Profile profile, House house, List<Review> reviewList) {
        return HouseIntroductionResponseDto.builder()
                .masterProfile(ProfileResponseDto.from(profile))
                .houseInfo(HouseResponseDto.from(house))
                .houseReviewList(reviewList.stream()
                        .filter(Review::isActivated)
                        .map(ReviewResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
