package com.example.mymoo.domain.account.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountExceptionDetails implements ExceptionDetails {
    // account id가 account 테이블에 존재하지 않을 때
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 account id입니다."),
    // 다른 계정에서 사용 중인 이메일로 회원가입을 시도할 때
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    // 충전된 포인트보다 더 많은 포인트를 사용하려 할 때
    NOT_ENOUGH_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    // 후원자가 아닌 계정이 권한을 변경하려 할 때
    NOT_ABLE_TO_CHANGE_ROLE(HttpStatus.BAD_REQUEST, "후원자 계정만 아동, 음식점 권한으로 변경이 가능합니다.")
    ;

    private final HttpStatus status;
    private final String message;
}