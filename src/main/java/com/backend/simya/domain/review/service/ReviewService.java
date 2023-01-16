package com.backend.simya.domain.review.service;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.backend.simya.domain.review.dto.ReviewRequestDto;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.review.repository.ReviewRepository;
import com.backend.simya.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;

    public ReviewResponseDto postReview(User currentUser, ReviewRequestDto reviewRequestDto) {
        Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
        Review savedReview = reviewRepository.save(reviewRequestDto.toEntity(mainProfile));

       return ReviewResponseDto.builder()
               .profileId(mainProfile.getProfileId())
               .nickname(mainProfile.getNickname())
               .comment(mainProfile.getComment())
               .reviewId(savedReview.getReviewId())
               .rate(savedReview.getRate())
               .content(savedReview.getContent())
               .build();
    }

    // 현재 사용자의 리뷰와 리뷰 쓴 프로필 찾기
    // 현재 사용자가 리뷰의 프로필의
//    public List<ReviewResponseDto> getReviewList(User currentUser) {
//        List<Profile> currentUsersProfileList = profileRepository.findProfilesByUser(currentUser);
//        List<Review> currentUsersReviewList = reviewRepository.findReviewsByProfile(mainProfile).stream()
//                .map(reviewResponseDto -> ReviewResponseDto.toDto)
//)
//
//
//        return null;
//    }
}
