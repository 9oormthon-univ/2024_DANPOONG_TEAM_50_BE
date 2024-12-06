package com.example.mymoo.global.auth.controller;

import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import com.example.mymoo.domain.email.EmailClient;
import com.example.mymoo.domain.email.dto.EmailSendDTO;
import com.example.mymoo.global.auth.dto.request.AccountLoginRequestDto;
import com.example.mymoo.global.auth.dto.request.TokenRefreshRequestDto;
import com.example.mymoo.global.auth.dto.response.AccountLoginResponseDto;
import com.example.mymoo.global.auth.dto.response.TokenRefreshResponseDto;
import com.example.mymoo.global.auth.service.AuthService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "[공통] 사용자 로그인",
        description = "Access Token은 매 요청시 Authorization 헤더에 `Bearer {ACCESS_TOKEN}`형식으로 넣어주세요. Refresh Token은 auth/token/refresh 요청 시 필요합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환, refresh token DB에 저장됨"),
        }
    )
    @PostMapping("/login")
    public ResponseEntity<AccountLoginResponseDto> login(
        @Valid @RequestBody AccountLoginRequestDto loginRequestDto
    ) {
        return ResponseEntity.ok(
            authService.login(loginRequestDto)
        );
    }

    @Operation(
        summary = "[공통] 사용자 로그아웃",
        description = "Access token과 refresh token을 지워주세요.",
        responses = {
            @ApiResponse(responseCode = "204", description = "로그아웃 성공, refresh token DB에서 삭제됨"),
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 현재 인증된 사용자 정보에서 userId 추출
        Long userId = userDetails.getAccountId();

        // 로그아웃 처리
        authService.logout(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @Operation(
        summary = "[공통] Access token 재발급",
        description = "Refresh token을 받아 검증 후 Access token을 재발급합니다. 다른 API 요청에서 `Expired JWT token` 에러 메시지가 오면 해당 API로 요청합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Access token 재발급 성공(유효기간 30분. Refresh token은 유효기간 하루. 로그인 시 둘 다 재생성)"),
        }
    )
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(
        @Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto
    ) {
        return ResponseEntity.ok(authService.getNewAccessToken(tokenRefreshRequestDto.refreshToken()));
    }
}
