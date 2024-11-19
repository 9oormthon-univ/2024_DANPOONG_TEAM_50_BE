package com.example.mymoo.global.oauth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record KakaoLoginResponseDto(
    Long accountId,
    String userRole,
    String accessToken,
    String refreshToken,
    @Schema(description = "신규 유저 여부. true일 시 음식점|후원자|결식아동 중 하나를 택하고 계정 별 추가 정보를 입력하는 화면으로 넘어감.")
    boolean isNewUser
) {

}