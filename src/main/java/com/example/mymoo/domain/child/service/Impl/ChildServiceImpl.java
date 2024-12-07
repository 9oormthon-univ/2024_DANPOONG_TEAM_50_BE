package com.example.mymoo.domain.child.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.entity.Location;
import com.example.mymoo.domain.child.exception.ChildException;
import com.example.mymoo.domain.child.exception.ChildExceptionDetails;
import com.example.mymoo.domain.child.repository.ChildRepository;
import com.example.mymoo.domain.child.repository.LocationRepository;
import com.example.mymoo.domain.child.service.ChildService;
import com.example.mymoo.global.aop.log.LogExecutionTime;
import com.example.mymoo.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional @LogExecutionTime
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;

    public Child createChild(
            final Long accountId,
            final String cardNumber,
            final String Do,
            final String sigun,
            final String gu
    ){
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(()->new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));

        if (childRepository.existsByAccount_Id(accountId)){
            throw new ChildException(ChildExceptionDetails.CHILD_ALREADY_EXISTS);
        }
        Child newChild = childRepository.save(
            Child.builder()
                .account(foundAccount)
                .cardNumber(cardNumber)
                .build()
        );

        locationRepository.save(
            Location.builder()
                .Do(Do)
                .sigun(sigun)
                .gu(gu)
                .child(newChild)
                .build()
        );
        foundAccount.changeUserRoleTo(UserRole.CHILD);
        return newChild;
    }
}
