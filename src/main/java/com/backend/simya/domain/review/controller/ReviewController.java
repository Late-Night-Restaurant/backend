package com.backend.simya.domain.review.controller;

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

import javax.validation.Valid;

import static com.backend.simya.global.common.BaseResponseStatus.*;


@Slf4j
@RestController
@RequestMapping("simya/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("")
    public BaseResponse<ReviewResponseDto> postReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            ReviewResponseDto reviewResponseDto = reviewService.postReview(currentUser, reviewRequestDto);
            return new BaseResponse<>(reviewResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(FAILED_TO_CREATE_REVIEW);
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

    @PatchMapping("/{reviewId}/delete")
    public BaseResponse<BaseResponseStatus> deleteReview(@PathVariable("reviewId") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new BaseResponse<>(SUCCESS_TO_DELETE_REVIEW);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


//    @GetMapping("")
//    public BaseResponse<List<ReviewResponseDto>> getMyReviewList() throws BaseException {
//        User currentUser = userService.getMyUserWithAuthorities();
//        List<ReviewResponseDto> reviewResponseDtoList = reviewService.getMyReviewList(currentUser);
//
//        return null;
//    }

//    @GetMapping("")
//    public BaseResponse<List<ReviewResponseDto>> getChattingRoomReviewList(@Valid @RequestBody ReviewResponseDto reviewDto) {
//
//        return null;
//    }
}
