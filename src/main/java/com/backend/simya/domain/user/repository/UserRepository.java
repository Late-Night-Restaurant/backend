package com.backend.simya.domain.user.repository;


import com.backend.simya.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 매핑되는 레포지토리
 * - JpaRepository를 extends 하면 기본적으로 findAll, save 등의 메소드를 사용할 수 있다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // username으로 User 정보를 가져올 때, 권한 정보도 함께 가져오게 해주는 메소드
//    @EntityGraph(attributePaths = "authorities")    // @EntityGraph: 쿼리가 수행될 때, Lazy 조회가 아닌 Eager 조회로 authorities 정보를 가져옴
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    // 이메일로 DB 에 동일한 계정이 있는지 조회
    Optional<User> findByEmail(String email);

    // 중복가입을 방지하기 위해 이메일 존재 여부 판단
    boolean existsByEmail(String email);
    boolean existsByPw(String password);
}
