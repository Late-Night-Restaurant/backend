package com.backend.simya.global.common;

import lombok.Getter;

//성공적인 응답은 200번대
//클라이언트 오류 응답은 400번대
//서버 오류 응답은 500번대
//리디렉션 응답은 300번대
@Getter
public enum BaseResponseStatus {

    // 200 ~ : 성공
    SUCCESS(true, 200, "요청에 성공했습니다."),
    SUCCESS_TO_UPDATE_REVIEW(true, 200, "리뷰 업데이트에 성공했습니다"),
    SUCCESS_TO_DELETE_REVIEW(true, 200, "리뷰 삭제에 성공했습니다."),
    SUCCESS_TO_REGISTER_FAVORITE(true, 200, "찜 등록에 성공했습니다."),
    SUCCESS_TO_CANCEL_FAVORITE(true, 200, "찜 해제에 성공했습니다."),
    NO_OPENED_HOUSE_YET(true, 200, "아직 오픈한 이야기 집이 없습니다."),
    NO_HOUSE_YET(true, 200, "이야기 집이 없습니다."),
    NO_REVIEWS_YET(true, 200, "아직 리뷰가 없습니다"),
    SUCCESS_TO_DELETE_TOPIC(true, 200, "오늘의 메뉴 삭제에 성공했습니다"),
    SUCCESS_TO_CLOSE_HOUSE(true, 200, "이야기 집을 마감했습니다"),
    SUCCESS_TO_DELETE_HOUSE(true, 200, "이야기 집을 폐점했습니다"),
    SUCCESS_TO_UPDATE_HOUSE_SIGNBOARD(true, 200, "이야기 집 간판을 수정했습니다."),
    NEVER_REVIEWED_BEFORE(true, 200, "리뷰를 쓴 적이 없는 유저입니다."),
    HAVE_REVIEWED_BEFORE(true, 200, "리뷰를 쓴 적이 있는 유저입니다."),
    SUCCESS_TO_UPDATE_CATEGORY(true, 200, "이야기 집의 전문메뉴를 수정했습니다."),

    SUCCESS_TO_WITHDRAW(true, 200, "회원탈퇴에 성공했습니다."),
    SUCCESS_TO_LOGOUT(true, 200, "로그아웃이 성공적으로 처리되었습니다."),
    SUCCESS_TO_UPDATE_PROFILE(true, 200, "프로필 수정이 완료되었습니다."),
    SUCCESS_TO_DELETE_PROFILE(true, 200, "프로필이 삭제되었습니다."),
    SUCCESS_TO_CHANGE_MAIN_PROFILE(true, 200, "메인 프로필이 변경되었습니다."),
    SUCCESS_TO_SIGNUP(true, 200, "회원가입에 성공했습니다"),





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
    FAILED_TO_FIND_USER(false, 500, "데이터베이스에서 해당 유저를 찾는데 실패했습니다"),

    USERS_NEED_ONE_MORE_PROFILE(false, 400, "모든 회원은 하나 이상의 프로필이 존재해야 합니다."),
    ALREADY_DELETE_PROFILE(false, 400, "이미 삭제된 프로필입니다."),
    PROFILE_NOT_FOUND(false, 404, "존재하지 않는 프로필입니다."),
    INVALID_FILE_TYPE(false, 404, "잘못된 형식의 파일입니다."),


    // house 관련
    FAILED_TO_OPEN(false, 403, "이야기 집 방장만 오픈이 가능합니다."),
    ALREADY_CATEGORY(false, 403, "이미 전문메뉴로 등록되어 있는 메뉴입니다"),
    FAILED_TO_UPDATE(false, 403, "이야기 집 방장만 수정이 가능합니다."),
    FAILED_TO_CLOSE(false, 403, "이야기 집 방장만 폐점이 가능합니다."),
    ONLY_MASTER_CAN_UPDATE(false, 403, "이야기 집 방장만 메뉴 수정이 가능합니다."),
    HOUSE_NOT_FOUND(false, 404, "존재하지 않는 이야기 집 입니다."),
    HOUSE_ALREADY_OPENED(false, 400, "이미 오픈된 이야기 집 입니다."),


    // 500 ~ : Database, Server 오류
    // 500 : Internal Server Error
    // 503 : Service Unavailable
    DATABASE_ERROR(false, 503, "데이터베이스 연결에 실패했습니다."),
    SERVER_ERROR(false, 503, "서버와의 연결에 실패했습니다."),

    FAILED_TO_JWT(false, 500, "토큰 발급에 실패했습니다."),
    FAILED_JWT_IN_HEADER(false, 500, "JWT 토큰이 헤더에 정상적으로 들어가지 않았습니다."),

    GET_FAIL_USERINFO(false, 500, "회원정보 조회에 실패했습니다"),
    POST_FAIL_USER(false, 500, "회원가입에 실패했습니다."),
    DELETE_FAIL_USER(false, 500, "회원탈퇴에 실패했습니다."),
    FAILED_TO_LOGOUT(false, 500, "로그아웃에 실패했습니다."),
    FAILED_TO_UPLOAD_IMAGE(false, 500, "이미지 업로드에 실패했습니다."),


    // [PATCH] user 정보 수정 시
    MODIFY_FAIL_USERNAME(false, 500, "회원 이름을 변경하는 데 실패했습니다."),
    MODIFY_FAIL_POSTS_INFO(false, 500, "게시글 정보 수정에 실패했습니다."),

    POST_FAIL_PROFILE(false, 500, "프로필 생성에 실패했습니다."),
    DELETE_FAIL_PROFILE(false, 500, "프로필 삭제에 실패했습니다."),
    UPDATE_FAIL_PROFILE(false, 500, "프로필 수정에 실패했습니다."),
    SET_FAIL_MAIN_PROFILE(false, 500, "대표 프로필 지정에 실패했습니다."),
    FAILED_TO_FIND_PROFILE(false, 500, "데이터베이스에서 프로필을 찾는데 실패했습니다"),


    // house 관련
    HOUSE_OPEN_FAILED(false, 500, "이야기 집 오픈에 실패했습니다."),
    FAILED_TO_UPDATE_MAIN_MENU(false, 500, "이야기 집 오픈에 실패했습니다."),
    FAILED_TO_CREATE_TOPIC(false, 500, "이야기 집 메뉴 생성에 실패했습니다"),
    HOUSE_UPDATE_FAILED(false, 500, "이야기 집 간판 수정에 실패했습니다"),
    FAILED_TO_OPEN_HOUSE(false, 500, "이야기 집 폐점에 실패했습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 500, "비밀번호 암호화에 실패했습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 500, "비밀번호 복호화에 실패했습니다."),
    FAILED_TO_LOAD_TODAY_TOPIC(false, 500, "오늘의 메뉴를 불러오는데 실패했습니다."),
    FAILED_TO_FIND_HOUSE(false, 500, "데이터베이스에서 이야기 집을 찾는데 실패했습니다"),
    FAILED_TO_FIND_PROFILES_LIKE_MY_HOUSE(false, 500, "데이터베이스에서 내 이야기 집을 찜한 프로필을 찾는데 실패했습니다."),
    FAILED_TO_FIND_MY_FAVORITE_HOUSE(false, 500, "데이터베이스에서 내가 찜한 이야기 집을 찾는데 실패했습니다."),
    FAILED_TO_DELETE_FAVORITE(false, 500, "데이터베이스에서 찜을 삭제하는데 실패했습니다."),
    FAILED_TO_FIND_HOUSES_REVIEW(false, 500, "데이터베이스에서 이야기 집에 달린 리뷰를 찾는데 실패했습니다."),







    //review 관련
    FAILED_TO_CREATE_REVIEW(false, 500, "데이터베이스에 리뷰를 저장하는데 실패했습니다."),
    FAILED_TO_FIND_REVIEW(false, 500, "리뷰를 찾을 수 없습니다."),
    FAILED_TO_FIND_MY_REVIEW(false, 500, "데이터베이스에서 내 리뷰들을 찾는데 실패했습니다."),
    FAILED_TO_DELETE_MY_REVIEW(false, 500, "데이터베이스에서 리뷰를 삭제하는데 실패했습니다."),
    FAILED_TO_UPDATE_MY_REVIEW(false, 500, "데이터베이스에서 리뷰를 업데이트하는데 실패했습니다."),
    FAILED_TO_CHECK_IS_REVIEWED(false, 500, "데이터베이스에서 리뷰작성 여부를 찾는데 실패했습니다"),


    FAILED_TO_FIND_FAVORITE(false, 500, "찜 목록을 찾을 수 없습니다."),
    FAILED_TO_CANCEL_FAVORITE(false, 500, "찜 해제에 실패했습니다."),
    FAILED_TO_REGISTER_FAVORITE(false, 500, "찜 등록에 실패했습니다."),


    // chat 관련
    FAILED_TO_SEND_MESSAGE(false, 500, "메시지 전송에 실패했습니다.");


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
