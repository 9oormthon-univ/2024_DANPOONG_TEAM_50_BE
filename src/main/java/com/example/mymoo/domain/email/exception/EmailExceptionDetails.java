package com.example.mymoo.domain.email.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailExceptionDetails implements ExceptionDetails {
    // 가게 id가 store 테이블에 존재하지 않을 때
    EMAIL_SEND_FAILED(HttpStatus.BAD_REQUEST, "이메일 전송이 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
