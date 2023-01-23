package com.backend.simya.domain.review.service;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.review.dto.MyReviewResponseDto;
import com.backend.simya.domain.review.dto.ReviewRequestDto;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.review.repository.ReviewRepository;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        try{
            Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
            Review savedReview = reviewRepository.save(reviewRequestDto.toEntity(mainProfile));
            mainProfile.addReview(savedReview);
            houseToReview.addReview(savedReview);
            return ReviewResponseDto.from(savedReview);
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_CREATE_REVIEW);
        }
    }

    public List<ReviewResponseDto> getHouseReviewList(House house) throws BaseException {
        try {
            List<ReviewResponseDto> houseReviewList = reviewRepository.findReviewsByHouse(house).stream()
                    .map(ReviewResponseDto::from)
                    .collect(Collectors.toList());
            if (houseReviewList.isEmpty()) {
                throw new BaseException(NO_REVIEWS_YET);
            } else {
                return houseReviewList;
            }
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_FIND_HOUSES_REVIEW);
        }
    }

//    public List<Review> getReviewList(Long houseId) {
//        return reviewRepository.findReviewsByHouseId(houseId);
//    }

    public List<MyReviewResponseDto> getCurrentProfileReviewList(User currentUser) throws BaseException {
        try{
            List<MyReviewResponseDto> myReviewList = reviewRepository.findReviewsByProfileId(currentUser.getProfileList()
                            .get(currentUser.getMainProfile()).getProfileId()).stream()
                    .map(review -> MyReviewResponseDto.from(review.getHouse(), review))
                    .collect(Collectors.toList());
            if (myReviewList.isEmpty()) {
                throw new BaseException(NO_REVIEWS_YET);
            } else {
                return myReviewList;
            }
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_FIND_MY_REVIEW);
        }
    }

    @Transactional
    public void deleteReview(Long reviewId) throws BaseException {
        try {
            Review findReview = findReview(reviewId);
            reviewRepository.delete(findReview);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_DELETE_MY_REVIEW);
        }
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto) throws BaseException {
        try {
            Review findReview = findReview(reviewId);
            Review updatedReview = findReview.updateReview(reviewRequestDto.getRate(), reviewRequestDto.getContent());
            return ReviewResponseDto.from(updatedReview);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_UPDATE_MY_REVIEW);
        }
    }

    private Review findReview(Long reviewId) throws BaseException {
        return reviewRepository.findById(reviewId).
                orElseThrow(() -> new BaseException(FAILED_TO_FIND_REVIEW));
    }

    public boolean isReviewedHouse(Long currentUserId, Long houseId) throws BaseException {
        try {
            return !reviewRepository.findReviewsByUserIdAndHouseId(currentUserId, houseId).isEmpty();
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_CHECK_IS_REVIEWED);
        }
    }

}
