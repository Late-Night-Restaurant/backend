package com.backend.simya.domain.review.service;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.backend.simya.domain.review.dto.MyReviewResponseDto;
import com.backend.simya.domain.review.dto.ReviewRequestDto;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.review.repository.ReviewRepository;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.simya.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponseDto postReview(User currentUser,
                                        House houseToReview,
                                        ReviewRequestDto reviewRequestDto) throws BaseException {
        Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
        Review savedReview = reviewRepository.save(reviewRequestDto.toEntity(mainProfile));
        mainProfile.addReview(savedReview);
        houseToReview.addReview(savedReview);

       return ReviewResponseDto.from(savedReview);
    }

    public List<ReviewResponseDto> getHouseReviewList(House house) {
        return reviewRepository.findReviewsByHouse(house).stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<Review> getReviewList(House house) {
        return reviewRepository.findReviewsByHouse(house);
    }

    public List<MyReviewResponseDto> getCurrentProfileReviewList(User currentUser) {
        return reviewRepository.findReviewsByProfileId(currentUser.getProfileList()
                        .get(currentUser.getMainProfile()).getProfileId()).stream()
                .map(review -> MyReviewResponseDto.from(review.getHouse(), review))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Long reviewId) throws BaseException {
        Review findReview = findReview(reviewId);
        reviewRepository.delete(findReview);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto) throws BaseException {
        Review findReview = findReview(reviewId);
        Review updatedReview = findReview.updateReview(reviewRequestDto.getRate(), reviewRequestDto.getContent());

        return ReviewResponseDto.from(updatedReview);
    }

    private Review findReview(Long reviewId) throws BaseException {
        return reviewRepository.findById(reviewId).
                orElseThrow(() -> new BaseException(FAILED_TO_FIND_REVIEW));
    }

    public boolean isReviewedHouse(Long currentUserId, Long houseId) {
        return !reviewRepository.findReviewsByUserIdAndHouseId(currentUserId, houseId).isEmpty();
    }
}
