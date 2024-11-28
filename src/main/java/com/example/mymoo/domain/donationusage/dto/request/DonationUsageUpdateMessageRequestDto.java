package com.example.mymoo.domain.donationusage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DonationUsageUpdateMessageRequestDto(
    @NotNull(message = "donationId는 필수 입력 항목입니다.")
    Long donationId,
    @NotBlank(message = "message는 필수 항목입니다")
    @Size(min = 1, max = 255, message = "message는 최소 1자, 최대 255자입니다.")
    String message
) {

}
