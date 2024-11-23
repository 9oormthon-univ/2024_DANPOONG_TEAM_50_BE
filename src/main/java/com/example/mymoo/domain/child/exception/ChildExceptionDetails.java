package com.example.mymoo.domain.child.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChildExceptionDetails implements ExceptionDetails {
    // child id가 child 테이블에 존재하지 않을 때
    CHILD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 child id입니다."),
    CHILD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 아동 회원가입 절차를 마쳤습니다."),
    CARD_NUMBER_BLANK(HttpStatus.BAD_REQUEST, "카드 번호를 보내주세요. cardNumber")
    ;

    private final HttpStatus status;
    private final String message;
}