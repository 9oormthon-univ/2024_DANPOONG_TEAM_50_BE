package com.example.mymoo.global.exception.handler;

import com.example.mymoo.global.exception.CustomException;
import com.example.mymoo.global.exception.ExceptionDetails;
import com.example.mymoo.global.exception.dto.BeanValidationExceptionResponseDto;
import com.example.mymoo.global.exception.dto.CustomExceptionResponseDto;
import com.example.mymoo.global.exception.dto.FieldErrorResponseDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponseDto> customExceptionHandler(CustomException exception) {
        ExceptionDetails exceptionDetails = exception.getExceptionDetails();
        log.error(
            "커스텀 예외 발생 {} : {}",
            exception.getClass().getSimpleName(),
            exceptionDetails.getMessage()
        );
        return ResponseEntity
            .status(exceptionDetails.getStatus())
            .body(CustomExceptionResponseDto.builder()
                .status(exceptionDetails.getStatus().value())
                .message(exceptionDetails.getMessage())
                .build()
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(BindingResult bindingResult) {
        List<FieldErrorResponseDto> fieldErrorResponseDtoList = bindingResult.getFieldErrors().stream()
            .map(fieldError -> FieldErrorResponseDto.builder()
                .name(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build())
            .toList();

        log.error("Validation 예외 발생: {}", fieldErrorResponseDtoList);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BeanValidationExceptionResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .messages(fieldErrorResponseDtoList)
                .build()
            );
    }
}
