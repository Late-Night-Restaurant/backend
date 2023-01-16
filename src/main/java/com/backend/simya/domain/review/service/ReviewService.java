package com.backend.simya.domain.review.service;

import com.backend.simya.domain.chattingroom.entity.ChattingRoom;
import com.backend.simya.domain.chattingroom.repository.ChattingRoomRepository;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;

    @Transactional
    public ReviewResponseDto postReview(User currentUser,
                                        ChattingRoom chattingRoomToReview,
                                        ReviewRequestDto reviewRequestDto) throws BaseException {
        Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
        Review savedReview = reviewRepository.save(reviewRequestDto.toEntity(mainProfile));
        mainProfile.addReview(savedReview);
        chattingRoomToReview.addReview(savedReview);

       return ReviewResponseDto.builder()
               .profileId(mainProfile.getProfileId())
               .nickname(mainProfile.getNickname())
               .comment(mainProfile.getComment())
               .reviewId(savedReview.getReviewId())
               .rate(savedReview.getRate())
               .content(savedReview.getContent())
               .build();
    }

    public List<ReviewResponseDto> getChattingRoomReviewList(ChattingRoom chattingRoom) {
        return reviewRepository.findReviewsByChattingRoom(chattingRoom).stream()
                .filter(Review::isActivated)
                .map(ReviewResponseDto::toDto)
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

        return ReviewResponseDto.builder()
                .profileId(findReview.getProfile().getProfileId())
                .nickname(findReview.getProfile().getNickname())
                .comment(findReview.getProfile().getComment())
                .reviewId(reviewId)
                .rate(updatedReview.getRate())
                .content(updatedReview.getContent())
                .build();
    }

    private Review findReview(Long reviewId) throws BaseException {
        return reviewRepository.findById(reviewId).
                orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_FIND_REVIEW));
    }


    // 리뷰를 썼을 때의 프로필을 포함한 현재 사용자의 리뷰찾기
    // 현재 사용자의 프로필을 전부 가져온 뒤, 각 프로필의 리뷰들을 가져와 리스트화 한다
//    public List<ReviewResponseDto> getMyReviewList(User currentUser) {
//        List<Review> currentUsersReviewList = new ArrayList<>();
//        currentUsersReviewList.addAll
//
//        profileRepository.findProfilesByUser(currentUser).stream()
//                .map(Profile::getReviewList)
//                .filter(Objects::nonNull)
//                .map()
//                .collect(Collectors.toList())
//
//        return null;
//    }
}
