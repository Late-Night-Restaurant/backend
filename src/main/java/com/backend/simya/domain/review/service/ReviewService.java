package com.backend.simya.domain.review.service;

import com.backend.simya.domain.house.entity.House;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

       return ReviewResponseDto.toDto(savedReview);
    }

    public List<ReviewResponseDto> getHouseReviewList(House house) {
        return reviewRepository.findReviewsByHouse(house).stream()
                .filter(Review::isActivated)
                .map(ReviewResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public List<MyReviewResponseDto> getMyReviewList(User currentUser) {
        return reviewRepository.findReviewsByUserId(currentUser.getUserId()).stream()
                .filter(Review::isActivated)
                .map(review -> MyReviewResponseDto.toDto(review.getHouse(), review))
                .collect(Collectors.toList());
    }

    public List<MyReviewResponseDto> getCurrentProfileReviewList(User currentUser) {
        return reviewRepository.findReviewsByProfileId(currentUser.getProfileList()
                        .get(currentUser.getMainProfile()).getProfileId()).stream()
                .filter(Review::isActivated)
                .map(review -> MyReviewResponseDto.toDto(review.getHouse(), review))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Long reviewId) throws BaseException {
        Review findReview = findReview(reviewId);
        findReview.changeStatus(false);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto) throws BaseException {
        Review findReview = findReview(reviewId);
        Review updatedReview = findReview.updateReview(reviewRequestDto.getRate(), reviewRequestDto.getContent());

        return ReviewResponseDto.toDto(updatedReview);
    }

    private Review findReview(Long reviewId) throws BaseException {
        return reviewRepository.findById(reviewId).
                orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_FIND_REVIEW));
    }

}
