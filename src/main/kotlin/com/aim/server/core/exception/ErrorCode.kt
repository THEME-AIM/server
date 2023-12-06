package com.aim.server.core.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val errorMessage: String
) {
    // * 공통 에러 처리
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력한 값이 올바르지 않습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다."),

    // * 유저 정보 관련 에러 처리
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "유저 인증에 실패하였습니다."),
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "유저 정보가 일치하지 않습니다."),
    USER_ALREADY_LOGGED_IN(HttpStatus.CONFLICT, "이미 로그인한 유저입니다."),
    INVALID_ID_OR_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),

    // * 관리자 정보 관련 에러 처리
    CONFIG_NOT_FOUND(HttpStatus.NOT_FOUND, "입력하신 정보와 관련된 설정이 존재하지 않습니다. 다시 한번 확인해주세요."),

    // * IP 정보 관련 에러 처리
    IP_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "입력하신 IP 주소와 관련된 정보가 존재하지 않습니다. 다시 한번 확인해주세요."),
    IP_ADDRESS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 할당된 IP 주소입니다."),
    MAC_ADDRESS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 할당된 MAC 주소입니다."),
    ;
}