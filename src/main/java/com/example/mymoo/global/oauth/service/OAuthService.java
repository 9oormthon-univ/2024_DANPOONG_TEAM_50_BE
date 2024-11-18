package com.example.mymoo.global.oauth.service;

import com.example.mymoo.global.oauth.dto.response.KakaoLoginResponseDto;

public interface OAuthService {
    String getKakaoAuthorizationUrl();
     KakaoLoginResponseDto processKakaoCallback(String code);
}
