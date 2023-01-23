package com.backend.simya.domain.favorite.service;

import com.backend.simya.domain.favorite.dto.MyFavoriteHouseResponseDto;
import com.backend.simya.domain.favorite.entity.Favorite;
import com.backend.simya.domain.favorite.repository.FavoriteRepository;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
import com.backend.simya.domain.profile.entity.Profile;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void registerFavorite(User currentUser, House houseToRegisterFavorite) {
        Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
        Favorite newFavorite = Favorite.builder()
                .profile(mainProfile)
                .house(houseToRegisterFavorite)
                .build();
        Favorite registeredFavorite = favoriteRepository.save(newFavorite);
        mainProfile.addFavorite(registeredFavorite);
        houseToRegisterFavorite.addFavorite(registeredFavorite);
    }

    @Transactional
    public void cancelFavorite(Long favoriteId) throws BaseException {
        try {
            Favorite favoriteToCancel = findFavorite(favoriteId);
            favoriteRepository.delete(favoriteToCancel);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_DELETE_FAVORITE);
        }
    }

    public List<MyFavoriteHouseResponseDto> findCurrentProfileFavoriteHouses(User currentUser) throws BaseException {
        try {
            Profile mainProfile = currentUser.getProfileList().get(currentUser.getMainProfile());
            return favoriteRepository.findFavoriteListByProfileId(mainProfile.getProfileId()).stream()
                    .map(MyFavoriteHouseResponseDto::from)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_FIND_MY_FAVORITE_HOUSE);
        }
    }

    public List<ProfileResponseDto> findProfilesLikeMyHouse(House house) throws BaseException {
        try {
            return favoriteRepository.findFavoriteListByHouseId(house.getHouseId()).stream()
                    .map(Favorite::getProfile)
                    .map(ProfileResponseDto::from)
                    .collect(Collectors.toList());
        } catch (Exception e) {
           throw new BaseException(FAILED_TO_FIND_PROFILES_LIKE_MY_HOUSE);
        }
    }

    public Favorite findFavorite(Long favoriteId) throws BaseException {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new BaseException(FAILED_TO_FIND_FAVORITE));
    }
}
