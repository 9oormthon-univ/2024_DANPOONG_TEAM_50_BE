package com.example.mymoo.global.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenRefreshResponseDto(
    String accessToken
) {

}
