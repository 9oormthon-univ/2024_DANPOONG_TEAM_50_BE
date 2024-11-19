package com.example.mymoo.domain.account.service.impl;

import com.example.mymoo.domain.account.dto.request.AccountCreateRequestDto;
import com.example.mymoo.domain.account.dto.request.ChargePointsRequestDto;
import com.example.mymoo.domain.account.dto.response.AccountCreateResponseDto;
import com.example.mymoo.domain.account.dto.response.ChargePointsResponseDto;
import com.example.mymoo.domain.account.dto.response.ReadAccountResponseDto;
import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.account.service.AccountService;
import com.example.mymoo.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    @Override
    public AccountCreateResponseDto signup(final AccountCreateRequestDto accountCreateRequestDto) {
        // 사용 가능한 이메일인지 확인
        if (accountRepository.existsByEmail(accountCreateRequestDto.email())) {
            throw new AccountException(AccountExceptionDetails.EMAIL_ALREADY_EXISTS);
        }

        Account savedAccount = accountRepository.save(
            Account.builder()
                .email(accountCreateRequestDto.email())
                .password(passwordEncoder.encode(accountCreateRequestDto.password()))
                .nickname(accountCreateRequestDto.nickname())
                .profileImageUrl(getDefaultImage())
                .phoneNumber(accountCreateRequestDto.phoneNumber())
                .point(0L)
                .role(UserRole.valueOf(accountCreateRequestDto.userRole()))
                .build()
        );

        return AccountCreateResponseDto.builder()
            .accountId(savedAccount.getId())
            .build();
    }

    @Override
    public ChargePointsResponseDto chargePoints(
        final Long accountId,
        final ChargePointsRequestDto chargePointsRequestDto
    ) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));

        account.chargePoint(chargePointsRequestDto.point());
        return ChargePointsResponseDto.builder()
            .point(account.getPoint())
            .build();
    }

    @Override
    public ReadAccountResponseDto getAccount(final Long accountId) {
        return ReadAccountResponseDto.from(
            accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND))
        );
    }

    private String getDefaultImage(){
        if (Math.random() < 0.5) {
            return "https://mymoo.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EC%9D%B4.png";
        } else {
            return "https://mymoo.s3.ap-northeast-2.amazonaws.com/%EB%AC%B4.png";
        }
    }
}
