package com.example.mymoo.domain.donation.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DonationExceptionDetails implements ExceptionDetails {
    // 입력 받은 donation id가 donation 테이블에 존재하지 않을 때
    DONATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 donation id입니다."),
    // 이미 사용된 후원을 사용하려 할 때
    DONATION_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용된 후원입니다."),
    // 자신의 후원이 아닌 다른 사람의 후원 내역을 읽으려 할 때
    FORBIDDEN_ACCESS_TO_OTHER_DONATOR(HttpStatus.FORBIDDEN, "해당 후원을 열람할 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}

