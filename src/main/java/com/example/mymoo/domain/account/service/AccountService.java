package com.example.mymoo.domain.account.service;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.request.ChargePointsRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.dto.response.ChargePointsResponseDto;
import com.example.mymoo.domain.account.dto.response.ReadAccountResponseDto;

public interface AccountService {
    AccountCreateResponseDto signup(final AccountCreateRequestDto accountCreateRequestDto);

    ChargePointsResponseDto chargePoints(
        final Long accountId,
        final ChargePointsRequestDto chargePointsRequestDto
    );

    ReadAccountResponseDto getAccount(final Long accountId);
}
