package com.example.mymoo.global.auth.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AccountLoginResponseDto(
    @NonNull Long accountId,
    @NonNull String userRole,
    @NonNull String accessToken,
    @NonNull String refreshToken
) {

}
