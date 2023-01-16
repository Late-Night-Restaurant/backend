package com.backend.simya.domain.house.controller;

import com.backend.simya.domain.house.dto.request.HouseOpenRequestDto;
import com.backend.simya.domain.house.dto.request.HouseRequestDto;
import com.backend.simya.domain.house.dto.request.HouseUpdateRequestDto;
import com.backend.simya.domain.house.dto.response.HouseResponseDto;
import com.backend.simya.domain.house.dto.response.HouseShowResponseDto;
import com.backend.simya.domain.house.service.HouseService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simya/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping("")
    public BaseResponse<HouseResponseDto> createHouseRoom(@RequestBody HouseRequestDto houseRequestDto) {
        try {
            return new BaseResponse(houseService.createHouse(houseRequestDto));
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PatchMapping("{houseId}")
    public BaseResponse<String> openHouse(@PathVariable("houseId") Long houseId, @RequestBody HouseOpenRequestDto houseOpenRequestDto) {
        try {
            houseService.openHouse(houseId, houseOpenRequestDto);
            return new BaseResponse<>("이야기 집이 오픈되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("{houseId}")
    public BaseResponse<HouseShowResponseDto> showHouse(@PathVariable("houseId") Long houseId) {
        try {
            return new BaseResponse<>(houseService.showHouse(houseId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/main/{houseId}")
    public BaseResponse<String> updateMain(@PathVariable("houseId") Long houseId, @RequestBody HouseUpdateRequestDto houseUpdateRequestDto) {
        try {
            houseService.updateMain(houseId, houseUpdateRequestDto);
            return new BaseResponse<>("이야기 집 간판이 수정되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/close/{houseId}")
    public BaseResponse<String> closeHouseRoom(@PathVariable("houseId") Long houseId) {
        try {
            houseService.closeHouseRoom(houseId);
            return new BaseResponse<>("이야기 집이 폐점되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
