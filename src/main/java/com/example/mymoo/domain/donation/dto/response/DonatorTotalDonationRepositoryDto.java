package com.example.mymoo.domain.donation.dto.response;

import java.time.LocalDateTime;

public record DonatorTotalDonationRepositoryDto(
    Long accountId,
    String donator,
    String profileImageUrl,
    Long totalDonation,
    LocalDateTime  lastDonatedAt
) {
}
