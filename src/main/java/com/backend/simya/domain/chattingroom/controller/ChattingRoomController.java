package com.backend.simya.domain.chattingroom.controller;

import com.backend.simya.domain.chattingroom.dto.request.ChattingOpenRequestDto;
import com.backend.simya.domain.chattingroom.dto.request.ChattingRequestDto;
import com.backend.simya.domain.chattingroom.dto.request.ChattingUpdateRequestDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingResponseDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingShowResponseDto;
import com.backend.simya.domain.chattingroom.service.ChattingRoomService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chattingRoom")
@RequiredArgsConstructor
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @PostMapping("")
    public BaseResponse<ChattingResponseDto> createChattingRoom(@RequestBody ChattingRequestDto chattingRequestDto) {
        try {
            return new BaseResponse(chattingRoomService.createChattingRoom(chattingRequestDto));
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PatchMapping("{chattingRoomId}")
    public BaseResponse<String> openChattingRoom(@PathVariable("chattingRoomId") Long chattingRoomId, @RequestBody ChattingOpenRequestDto chattingOpenRequestDto) {
        try {
            chattingRoomService.openChattingRoom(chattingRoomId, chattingOpenRequestDto);
            return new BaseResponse<>("이야기 집이 오픈되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("{chattingRoomId}")
    public BaseResponse<ChattingShowResponseDto> showChattingRoom(@PathVariable("chattingRoomId") Long chattingRoomId) {
        try {
            return new BaseResponse<>(chattingRoomService.showChattingRoom(chattingRoomId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/main/{chattingRoomId}")
    public BaseResponse<String> updateMain(@PathVariable("chattingRoomId") Long chattingRoomId, @RequestBody ChattingUpdateRequestDto chattingUpdateRequestDto) {
        try {
            chattingRoomService.updateMain(chattingRoomId, chattingUpdateRequestDto);
            return new BaseResponse<>("이야기 집 간판이 수정되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/close/{chattingRoomId}")
    public BaseResponse<String> closeChattingRoom(@PathVariable("chattingRoomId") Long chattingRoomId) {
        try {
            chattingRoomService.closeChattingRoom(chattingRoomId);
            return new BaseResponse<>("이야기 집이 폐점되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
