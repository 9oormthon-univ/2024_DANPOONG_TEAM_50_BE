package com.example.mymoo.domain.account.exception;

import com.example.mymoo.global.auth.exception.AuthExceptionDetails;
import com.example.mymoo.global.exception.CustomException;

public class AccountException extends CustomException {
    public AccountException(AccountExceptionDetails accountExceptionDetails){
        super(accountExceptionDetails);
    }
}