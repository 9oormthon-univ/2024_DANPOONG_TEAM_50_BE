package com.example.mymoo.domain.payment.exception;

import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.global.exception.CustomException;

public class PaymentException extends CustomException {
    public PaymentException(PaymentExceptionDetails paymentExceptionDetails){
        super(paymentExceptionDetails);
    }
}
