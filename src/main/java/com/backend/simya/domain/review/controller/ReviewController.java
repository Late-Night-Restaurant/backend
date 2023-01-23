package com.backend.simya.domain.review.controller;

import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.review.dto.MyReviewResponseDto;
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
@RequestMapping("simya/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final HouseService houseService;


    @PostMapping("/{houseId}")
    public BaseResponse<ReviewResponseDto> postReview(@PathVariable("houseId") Long houseId,
                                                      @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            House houseToReview = houseService.findHouse(houseId);
            ReviewResponseDto reviewResponseDto = reviewService.postReview(currentUser, houseToReview, reviewRequestDto);
            return new BaseResponse<>(reviewResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{houseId}")
    public BaseResponse<List<ReviewResponseDto>> getHouseReviewList(@PathVariable("houseId") Long houseId) {
        try {
            House findHouse = houseService.findHouse(houseId);
            List<ReviewResponseDto> houseReviewList = reviewService.getHouseReviewList(findHouse);
            return new BaseResponse<>(houseReviewList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/my")
    public BaseResponse<List<MyReviewResponseDto>> getCurrentProfileReviewList() {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            List<MyReviewResponseDto> myReviewList = reviewService.getCurrentProfileReviewList(currentUser);
            return new BaseResponse<>(myReviewList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(@PathVariable("reviewId") Long reviewId,
                                                        @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            ReviewResponseDto updatedReviewDto = reviewService.updateReview(reviewId, reviewRequestDto);
            return new BaseResponse<>(SUCCESS_TO_UPDATE_REVIEW, updatedReviewDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{reviewId}")
    public BaseResponse<BaseResponseStatus> deleteReview(@PathVariable("reviewId") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new BaseResponse<>(SUCCESS_TO_DELETE_REVIEW);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/check/{houseId}")
    public BaseResponse<BaseResponseStatus> checkReviewedHouse(@PathVariable("houseId") Long houseId) {
        try {
            Long currentUserId = userService.getMyUserWithAuthorities().getUserId();
            if (reviewService.isReviewedHouse(currentUserId, houseId)) {
                return new BaseResponse<>(HAVE_REVIEWED_BEFORE);
            } else {
                return new BaseResponse<>(NEVER_REVIEWED_BEFORE);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
