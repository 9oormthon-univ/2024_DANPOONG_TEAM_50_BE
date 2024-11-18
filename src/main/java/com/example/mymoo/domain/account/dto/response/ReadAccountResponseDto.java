package com.example.mymoo.domain.account.dto.response;

import com.example.mymoo.domain.account.entity.Account;
import lombok.Builder;

@Builder
public record ReadAccountResponseDto(
    Long accountId,
    String email,
    String phone_number,
    String nickname,
    Long point,
    String profileImageUrl
) {
    public static ReadAccountResponseDto from(Account account){
        return ReadAccountResponseDto.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .phone_number(account.getPhoneNumber())
            .nickname(account.getNickname())
            .point(account.getPoint())
            .profileImageUrl(account.getProfileImageUrl())
            .build();
    }
}
