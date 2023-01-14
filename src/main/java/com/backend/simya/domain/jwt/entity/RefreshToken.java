package com.backend.simya.domain.jwt.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken {

    // DB의 인덱스 용도로 저장
    @Id
    @Column(name = "rt_key", nullable = false)
    private String key;  // user의 email(username) 값이 들어감

    @Column(name = "rt_value", nullable = false)
    private String value;  // Refresh Token String

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateToken(String token) {
        this.value = token;
        return this;
    }
}

