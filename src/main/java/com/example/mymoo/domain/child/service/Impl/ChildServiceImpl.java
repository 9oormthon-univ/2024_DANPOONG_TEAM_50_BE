package com.example.mymoo.domain.child.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.child.dto.request.ChildReqeustDTO;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.exception.ChildException;
import com.example.mymoo.domain.child.exception.ChildExceptionDetails;
import com.example.mymoo.domain.child.repository.ChildRepository;
import com.example.mymoo.domain.child.service.ChildService;
import com.example.mymoo.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final AccountRepository accountRepository;

    public Child createChild(ChildReqeustDTO request){
        return null;
    }
}
