package com.backend.simya.domain.profile.service;


import com.backend.simya.domain.favorite.entity.Favorite;
import com.backend.simya.domain.profile.dto.request.ProfileRequestDto;
import com.backend.simya.domain.profile.dto.request.ProfileUpdateDto;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.profile.repository.ProfileRepository;
import com.backend.simya.domain.review.entity.Review;
import com.backend.simya.domain.user.entity.User;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.backend.simya.global.common.BaseResponseStatus.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ProfileResponseDto createProfile(ProfileRequestDto profileRequestDto,
                                            MultipartFile profileImage,
                                            User currentUser) throws BaseException {
        try {
            String profileImageUrl = s3Uploader.uploadImage(profileImage);
            Profile savedProfile = profileRepository.save(profileRequestDto.toEntity(false, profileImageUrl));
            currentUser.addProfile(savedProfile);
            return ProfileResponseDto.from(savedProfile);
        } catch (Exception ignored) {
            throw new BaseException(POST_FAIL_PROFILE);
        }
    }

    @Transactional
    public ProfileResponseDto updateProfile(ProfileUpdateDto profileUpdateDto, Long profileId) throws BaseException {
        try {
            Profile profile = findProfile(profileId);
            return  ProfileResponseDto.from(profile.update(profileUpdateDto));
        } catch (Exception ignored) {
            throw new BaseException(UPDATE_FAIL_PROFILE);
        }
    }

    // 대표 프로필은 하나만 지정 가능
    public void setMainProfile(Long profileId) throws BaseException {
        Profile profile = findProfile(profileId);

        try {
            User user = profile.getUser();
            if (user == null) {
                throw new BaseException(GET_FAIL_USERINFO);
            }

            int idx = user.getMainProfile();  // 이미 대표 프로필이 존재한다면 해제 후 선택
            log.info("현재 유저의 main profile index: {}", idx);

            if (idx == -1) {  // 대표 프로필로 지정된 것이 없다면 바로 선택
                profile.selectMainProfile();
                log.info("변경 후 main profile index: {}", user.getMainProfile());
            } else {
                user.getProfileList().get(idx).cancelMainProfile();
                profile.selectMainProfile();
            }
        } catch (IndexOutOfBoundsException exception) {
            throw new BaseException(USERS_NEED_ONE_MORE_PROFILE);
        } catch (Exception ignored) {
            throw new BaseException(SET_FAIL_MAIN_PROFILE);
        }
    }

    @Transactional
    public void deleteProfile(Long profileId) throws BaseException {
        try {
            Profile profileToDelete = findProfile(profileId);
            boolean isMain = profileToDelete.isRepresent();
            if (profileToDelete.getUser().getProfileList().isEmpty()) {
                throw new BaseException(USERS_NEED_ONE_MORE_PROFILE);
            } else {
                if (isMain) {
                    profileToDelete.autoSetMainProfile();
                } else {
                    s3Uploader.deleteImage(profileToDelete.getPictureUrl());
                    profileRepository.delete(profileToDelete);
                }
            }
        } catch (Exception ignored) {
            throw new BaseException(DELETE_FAIL_PROFILE);
        }
    }

    public Profile findProfile(Long profileId) throws BaseException {
        return profileRepository.findById(profileId).orElseThrow(
                () -> new BaseException(PROFILE_NOT_FOUND)
        );
    }

    public List<ProfileResponseDto> findMyAllProfile(User currentUser) throws BaseException {
        try {
            return profileRepository.findProfilesByUser(currentUser).stream()
                    .map(ProfileResponseDto::from)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_FIND_PROFILE);
        }
    }

    @Transactional
    public void deleteReview(Profile currentProfile, Review review) {
        currentProfile.removeReview(review);
    }

    @Transactional
    public void deleteFavorite(Profile currentProfile, Favorite favorite) {
        currentProfile.removeFavorite(favorite);
    }
}
