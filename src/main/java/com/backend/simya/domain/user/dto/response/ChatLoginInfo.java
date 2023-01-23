package com.backend.simya.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatLoginInfo {

    private String name;
    private String token;

    @Builder
    public ChatLoginInfo(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
