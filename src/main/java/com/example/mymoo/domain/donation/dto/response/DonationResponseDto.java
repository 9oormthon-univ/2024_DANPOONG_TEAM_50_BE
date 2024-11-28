package com.example.mymoo.domain.donation.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DonationResponseDto(
    @NotBlank
    String donator
) {

}
