package com.backend.simya.domain.review.controller;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.review.dto.ReviewRequestDto;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.service.ReviewService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import com.backend.simya.global.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.simya.global.common.BaseResponseStatus.*;


@Slf4j
@RestController
@RequestMapping("simya/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final HouseService houseService;

    @PostMapping("/{house-id}")
    public BaseResponse<ReviewResponseDto> postReview(@PathVariable("house-id") Long houseId,
                                                      @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            House houseToReview = houseService.getHouse(houseId);
            ReviewResponseDto reviewResponseDto = reviewService.postReview(currentUser, houseToReview, reviewRequestDto);
            return new BaseResponse<>(reviewResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(FAILED_TO_CREATE_REVIEW);
        }
    }

    @GetMapping("/{house-id}")
    public BaseResponse<List<ReviewResponseDto>> getHouseReviewList(@PathVariable("house-id") Long houseId) {
        try {
            House findHouse = houseService.getHouse(houseId);
            List<ReviewResponseDto> houseReviewList = reviewService.getHouseReviewList(findHouse);
            if (houseReviewList.isEmpty()) {
                return new BaseResponse<>(NO_REVIEWS_YET);
            } else {
                return new BaseResponse<>(houseReviewList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/all")
    public BaseResponse<List<ReviewResponseDto>> getMyReviewList() {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            List<ReviewResponseDto> myReviewList = reviewService.getMyReviewList(currentUser);
            if (myReviewList.isEmpty()) {
                return new BaseResponse<>(NO_REVIEWS_YET);
            } else {
                return new BaseResponse<>(myReviewList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/profile")
    public BaseResponse<List<ReviewResponseDto>> getCurrentProfileReviewList() {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            List<ReviewResponseDto> myReviewList = reviewService.getCurrentProfileReviewList(currentUser);
            if (myReviewList.isEmpty()) {
                return new BaseResponse<>(NO_REVIEWS_YET);
            } else {
                return new BaseResponse<>(myReviewList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }



    @PatchMapping("/{review-id}")
    public BaseResponse<ReviewResponseDto> updateReview(@PathVariable("review-id") Long reviewId,
                                              @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            ReviewResponseDto updatedReviewDto = reviewService.updateReview(reviewId, reviewRequestDto);
            return new BaseResponse<>(SUCCESS_TO_UPDATE_REVIEW, updatedReviewDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{review-id}/delete")
    public BaseResponse<BaseResponseStatus> deleteReview(@PathVariable("review-id") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new BaseResponse<>(SUCCESS_TO_DELETE_REVIEW);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
