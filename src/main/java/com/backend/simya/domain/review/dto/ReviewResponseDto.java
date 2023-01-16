package com.backend.simya.domain.review.dto;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.User;
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
public class ReviewResponseDto {

    private Long profileId;

    private String nickname;

    private String comment;

    private Long reviewId;

    private int rate;

    private String content;

    public static ReviewResponseDto toDto(Profile profile, Review review) {
        return ReviewResponseDto.builder()
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .comment(profile.getComment())
                .reviewId(review.getReviewId())
                .rate(review.getRate())
                .content(review.getContent())
                .build();
    }
}
