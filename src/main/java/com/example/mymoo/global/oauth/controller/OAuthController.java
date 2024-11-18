package com.example.mymoo.global.oauth.controller;

import com.example.mymoo.global.oauth.dto.response.KakaoLoginResponseDto;
import com.example.mymoo.global.oauth.dto.response.KakaoLoginUrlResponseDto;
import com.example.mymoo.global.oauth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @Operation(
        summary = "[공통] 카카오 로그인",
        description = "카카오 로그인 화면 URL을 반환합니다. 해당 URL로 이동해주세요.",
        responses = {
            @ApiResponse(responseCode = "200", description = "이동 후 사용자가 카카오 로그인을 마칠 시 카카오 인증 서버는 /kakao/callback으로 redirect 시킵니다."),
        }
    )
    @GetMapping("/kakao")
    public ResponseEntity<KakaoLoginUrlResponseDto> kakaoLogin() {
        return ResponseEntity.ok(
            KakaoLoginUrlResponseDto.builder()
            .authorizationUrl(oAuthService.getKakaoAuthorizationUrl())
            .build()
        );
    }

    @Operation(
        summary = "[공통] 카카오 서버가 사용자로 하여금 redirect 시키는 API endpoint",
        description = "서버의 redirect 응답을 그대로 수행하면, 해당 API로 인가 코드와 함께 요청을 보내고, 서버는 카카오 계정 정보를 이용해 로그인 또는 회원가입 절차(id, pw 저장)를 처리해 결과를 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "isNewUser는 신규 회원인지의 여부. 신규 회원이라면 음식점|후원자|결식아동 중 하나를 선택하라는 화면으로 이동."),
        }
    )
    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponseDto> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(oAuthService.processKakaoCallback(code));
    }
}
