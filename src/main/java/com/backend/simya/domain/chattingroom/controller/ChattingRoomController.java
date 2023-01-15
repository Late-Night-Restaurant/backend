package com.backend.simya.domain.chattingroom.controller;

import com.backend.simya.domain.chattingroom.dto.request.ChattingOpenRequestDto;
import com.backend.simya.domain.chattingroom.dto.request.ChattingRoomRequestDto;
import com.backend.simya.domain.chattingroom.dto.response.ChattingRoomResponseDto;
import com.backend.simya.domain.chattingroom.service.ChattingRoomService;
import com.backend.simya.global.common.BaseException;
import com.backend.simya.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chattingRoom")
@RequiredArgsConstructor
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @PostMapping("")
    public BaseResponse<ChattingRoomResponseDto> createChattingRoom(@RequestBody ChattingRoomRequestDto chattingRequestDto) {
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


}
