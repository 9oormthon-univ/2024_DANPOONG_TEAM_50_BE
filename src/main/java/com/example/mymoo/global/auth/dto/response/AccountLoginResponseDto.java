package com.example.mymoo.global.auth.dto.response;

import lombok.Builder;

@Builder
public record AccountLoginResponseDto(
    Long accountId,
    String userRole,
    String accessToken,
    String refreshToken
) {

}
