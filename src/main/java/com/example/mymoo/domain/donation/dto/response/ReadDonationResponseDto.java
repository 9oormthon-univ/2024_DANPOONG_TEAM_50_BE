package com.example.mymoo.domain.donation.dto.response;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReadDonationResponseDto(
    @NonNull String store,
    @NonNull LocalDateTime donatedAt,
    @NonNull Long point,
    LocalDateTime donationUsedAt,
    String message,
    String child,
    String childProfileUrl
) {
    public static ReadDonationResponseDto from(Donation donation){
        return ReadDonationResponseDto.builder()
            .store(donation.getStore().getName())
            .donatedAt(donation.getCreatedAt())
            .point(donation.getPoint())
            .build();
    }
    public static ReadDonationResponseDto from(Donation donation, DonationUsage donationUsage){
        Account childAccount = donationUsage.getChild().getAccount();
        return ReadDonationResponseDto.builder()
            .store(donation.getStore().getName())
            .donatedAt(donation.getCreatedAt())
            .point(donation.getPoint())
            .donationUsedAt(donationUsage.getCreatedAt())
            .message(donationUsage.getMessage())
            .child(childAccount.getNickname())
            .childProfileUrl(childAccount.getProfileImageUrl())
            .build();
    }
}
