package com.backend.simya.domain.review.controller;

import com.backend.simya.domain.review.dto.ReviewRequestDto;
import com.backend.simya.domain.review.dto.ReviewResponseDto;
import com.backend.simya.domain.review.service.ReviewService;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.domain.user.service.UserService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("")
    public BaseResponse<ReviewResponseDto> postReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) throws BaseException {
        User currentUser = userService.getMyUserWithAuthorities();
        ReviewResponseDto reviewResponseDto = reviewService.postReview(currentUser, reviewRequestDto);

        return new BaseResponse<>(reviewResponseDto);
    }

    @GetMapping("")
    public BaseResponse<List<ReviewResponseDto>> getMyReviewList() throws BaseException {
        User currentUser = userService.getMyUserWithAuthorities();
        List<ReviewResponseDto> reviewResponseDtoList = reviewService.getReviewList(currentUser);

        return null;
    }

//    @GetMapping("")
//    public BaseResponse<List<ReviewResponseDto>> getChattingRoomReviewList(@Valid @RequestBody ReviewResponseDto reviewDto) {
//
//        return null;
//    }
}
