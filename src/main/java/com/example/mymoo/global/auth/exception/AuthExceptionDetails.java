package com.example.mymoo.global.auth.exception;

import com.example.mymoo.global.exception.ExceptionDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum AuthExceptionDetails implements ExceptionDetails {
    // redis에서 사용자의 id에 대응되는 refresh key를 못 찾았을 때
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 한 이력이 없습니다. 다시 로그인 해주세요."),
    // 사용자 로그인 시 전달했던 refresh token과 입력받은 refresh token이 일치하지 않을 때
    NOT_VALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 Refresh 토큰입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
