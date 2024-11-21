package com.example.mymoo.domain.donation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DonationRequestDto(
    @NotNull(message = "후원 포인트는 필수 입력 항목입니다.")
    @Min(value = 1000, message = "후원 포인트는 1000 이상의 값이어야 합니다.")
    @Max(value = 20000, message = "후원 포인트는 20,000 이하의 값이어야 합니다.")
    Long point
) {

}
