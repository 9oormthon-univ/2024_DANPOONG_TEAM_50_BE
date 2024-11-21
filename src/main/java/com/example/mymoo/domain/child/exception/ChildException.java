package com.example.mymoo.domain.child.exception;

import com.example.mymoo.global.exception.CustomException;

public class ChildException extends CustomException {
    public ChildException(ChildExceptionDetails childExceptionDetails){
        super(childExceptionDetails);
    }
}