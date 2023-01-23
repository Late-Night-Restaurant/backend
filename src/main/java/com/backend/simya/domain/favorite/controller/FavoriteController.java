package com.backend.simya.domain.favorite.controller;

import com.backend.simya.domain.favorite.dto.MyFavoriteHouseResponseDto;
import com.backend.simya.domain.favorite.service.FavoriteService;
import com.backend.simya.domain.house.entity.House;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.domain.profile.dto.response.ProfileResponseDto;
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
@RequiredArgsConstructor
@RequestMapping("simya/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final HouseService houseService;


    @PostMapping("/{houseId}")
    public BaseResponse<BaseResponseStatus> registerFavorite(@PathVariable("houseId") Long houseId) {
        try {
            User currentUser = userService.getMyUserWithAuthorities();
            House houseToRegisterFavorite = houseService.findHouse(houseId);
            favoriteService.registerFavorite(currentUser, houseToRegisterFavorite);
            return new BaseResponse<>(SUCCESS_TO_REGISTER_FAVORITE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{favoriteId}")
    public BaseResponse<BaseResponseStatus> cancelFavorite(@PathVariable("favoriteId") Long favoriteId) {
        try {
            favoriteService.cancelFavorite(favoriteId);
            return new BaseResponse<>(SUCCESS_TO_CANCEL_FAVORITE);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/my")
    public BaseResponse<List<MyFavoriteHouseResponseDto>> getMyFavoriteHouses() {
        try{
            User currentUser = userService.getMyUserWithAuthorities();
            return new BaseResponse<>(favoriteService.findCurrentProfileFavoriteHouses(currentUser));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{houseId}")
    public BaseResponse<List<ProfileResponseDto>> getProfilesLikeMyHouse(@PathVariable("houseId") Long houseId) {
        try{
            House findHouse = houseService.findHouse(houseId);
            return new BaseResponse<>(favoriteService.findProfilesLikeMyHouse(findHouse));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
