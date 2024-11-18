package com.example.mymoo.global.exception.dto;

import lombok.Builder;

@Builder
public record CustomExceptionResponseDto(
    int status,
    String message
) {

}
