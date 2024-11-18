package com.example.mymoo.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionDetails {

    HttpStatus getStatus();

    String getMessage();
}
