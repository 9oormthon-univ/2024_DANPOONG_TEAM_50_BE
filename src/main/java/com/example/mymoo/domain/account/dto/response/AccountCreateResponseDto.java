package com.example.mymoo.domain.account.dto.response;

import lombok.Builder;

@Builder
public record AccountCreateResponseDto(
    Long accountId
) {

}
