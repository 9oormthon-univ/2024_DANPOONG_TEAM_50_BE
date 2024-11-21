package com.example.mymoo.domain.store.exception;

import com.example.mymoo.global.exception.CustomException;

public class StoreException extends CustomException {
    public StoreException(StoreExceptionDetails storeExceptionDetails){
        super(storeExceptionDetails);
    }
}
