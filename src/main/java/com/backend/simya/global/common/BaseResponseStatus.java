package com.backend.simya.global.common;

import lombok.Getter;

//성공적인 응답은 200번대
//클라이언트 오류 응답은 400번대
//서버 오류 응답은 500번대
//리디렉션 응답은 300번대
@Getter
public enum BaseResponseStatus {

    // 200 ~ : 성공
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    // 400 ~ :  클라이언트 Request 오류
    // 400 : Bad Request
    // 401 : Unauthorized
    // 403 : Forbidden
    // 404 : Not Found

    REQUEST_ERROR(false, 400, "입력값을 확인해주세요"),
    EMPTY_JWT(false, 401, "JWT를 입력해주세요."),
    INVALID_JWT(false, 403, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 403, "권한이 없는 유저의 접근입니다."),
    EXPIRED_JWT(false, 401, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(false, 401, "지원하지 않는 JWT 토큰입니다."),
    MALFORMED_JWT(false, 401, "잘못된 JWT 서명입니다."),

    // user 관련
    USERS_INVALID_ACCESS(false, 401, "잘못된 인가 접근입니다."),
    USERS_NOT_AUTHORIZED(false, 401, "인가되지 않은 사용자입니다."),
    USERS_NOT_FOUND(false, 400, "존재하지 않는 사용자입니다."),
    USERS_EMPTY_USER_ID(false, 400, "유저 아이디 값을 확인해주세요"),

    POST_USERS_EXISTS_EMAIL(false, 400, "중복된 이메일입니다."),

    POST_USERS_EMPTY_PASSWORD(false, 400, "비밀번호를 입력해주세요."),
    USERS_PASSWORD_FORMAT(false, 400, "비밀번호는 8자 이상의 영문 대소문자와 특수문자로 구성해야 합니다."),

    ACCESS_ONLY_ADMIN(false, 400, "관리자만 접근 가능합니다."),


    // user 관련
    FAILED_TO_LOGIN(false, 404, "존재하지 않는 아이디이거나 비밀번호가 틀렸습니다."),
    BANNED_USER_IN_LOGIN(false, 403, "정지된 유저이므로 로그인이 불가합니다."),



    // 500 ~ : Database, Server 오류
    // 500 : Internal Server Error
    // 503 : Service Unavailable
    DATABASE_ERROR(false, 503, "데이터베이스 연결에 실패했습니다."),
    SERVER_ERROR(false, 503, "서버와의 연결에 실패했습니다."),

    FAILED_TO_JWT(false, 500, "토큰 발급에 실패했습니다."),
    FAILED_JWT_IN_HEADER(false, 500, "JWT 토큰이 헤더에 정상적으로 들어가지 않았습니다."),

    GET_FAIL_USERINFO(false, 500, "회원정보 조회에 실패했습니다"),
    POST_FAIL_USER(false, 500, "회원가입에 실패했습니다."),

    // [PATCH] user 정보 수정 시
    MODIFY_FAIL_USERNAME(false, 500, "회원 이름을 변경하는 데 실패했습니다."),
    MODIFY_FAIL_POSTS_INFO(false, 500, "게시글 정보 수정에 실패했습니다."),

    DELETE_FAIL_PROFILE(false, 500, "프로필 삭제에 실패했습니다."),
    UPDATE_FAIL_PROFILE(false, 500, "프로필 수정에 실패했습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 500, "비밀번호 암호화에 실패했습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 500, "비밀번호 복호화에 실패했습니다.");

    /**
     * 5000, 6000 : 필요 시 추가 구현
     */


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
