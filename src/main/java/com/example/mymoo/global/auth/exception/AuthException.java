package com.example.mymoo.global.auth.exception;

import com.example.mymoo.global.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(AuthExceptionDetails authExceptionDetails){
        super(authExceptionDetails);
    }
}
