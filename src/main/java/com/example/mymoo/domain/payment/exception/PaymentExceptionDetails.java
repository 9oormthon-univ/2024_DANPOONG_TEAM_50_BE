package com.example.mymoo.domain.payment.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionDetails implements ExceptionDetails {
    // 가게 id가 store 테이블에 존재하지 않을 때
    APPROVE_FAILED(HttpStatus.BAD_REQUEST, "승인 요청이 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
