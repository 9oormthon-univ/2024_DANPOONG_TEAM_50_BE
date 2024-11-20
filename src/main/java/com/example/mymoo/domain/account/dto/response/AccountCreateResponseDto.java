package com.example.mymoo.domain.account.dto.response;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AccountCreateResponseDto(
    @NonNull Long accountId
) {

}
