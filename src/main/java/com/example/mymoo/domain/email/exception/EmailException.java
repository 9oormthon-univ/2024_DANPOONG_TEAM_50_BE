package com.example.mymoo.domain.email.exception;

import com.example.mymoo.global.exception.CustomException;

public class EmailException extends CustomException {
    public EmailException(EmailExceptionDetails paymentExceptionDetails){
        super(paymentExceptionDetails);
    }
}
