package com.backend.simya.domain.jwt.repository;

import com.backend.simya.domain.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 토큰 존재 유무 확인
    Optional<RefreshToken> findByKey(String key);
    Optional<RefreshToken> findByValue(String token);
}
