package com.example.mymoo.global.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequestDto(
    @NotBlank(message = "refreshToken은 필수 항목입니다")
    String refreshToken
) {

}
