package com.backend.simya.domain.review.dto;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private int rate;

    private String content;

    public Review toEntity(Profile profile) {
        return Review.builder()
                .profile(profile)
                .rate(this.rate)
                .content(this.content)
                .activated(true)
                .build();
    }
}