package com.backend.simya.domain.review.dto;

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
public class ReviewResponseDto {

    private Long profileId;

    private String nickname;

    private String comment;

    private Long reviewId;

    private int rate;

    private String content;

    public static ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .profileId(review.getProfile().getProfileId())
                .nickname(review.getProfile().getNickname())
                .comment(review.getProfile().getComment())
                .reviewId(review.getReviewId())
                .rate(review.getRate())
                .content(review.getContent())
                .build();
    }
}
