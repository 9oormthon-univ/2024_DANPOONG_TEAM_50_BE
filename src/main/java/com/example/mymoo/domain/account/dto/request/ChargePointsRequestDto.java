package com.example.mymoo.domain.account.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChargePointsRequestDto(
    @NotNull(message = "충전 포인트는 필수 입력 항목입니다.")
    @Min(value = 50_000, message = "충전 포인트는 50,000 이상의 값이어야 합니다.")
    @Max(value = 1_000_000, message = "충전 포인트는 1,000,000 이하의 값이어야 합니다.")
    Long point
) {

}
