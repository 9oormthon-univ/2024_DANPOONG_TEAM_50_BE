package com.example.mymoo.domain.account.dto.response;

import com.example.mymoo.domain.account.entity.Account;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReadAccountResponseDto(
    @NonNull Long accountId,
    @NonNull String email,
    String phone_number,
    @NonNull String nickname,
    @NonNull Long point,
    @NonNull String profileImageUrl
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
