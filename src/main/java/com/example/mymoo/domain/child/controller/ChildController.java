package com.example.mymoo.domain.child.controller;

import com.example.mymoo.domain.child.dto.request.ChildCreateRequestDto;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.service.ChildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;
    @Operation(
        summary = "[공통] 아동 추가 회원가입 및 후원자 계정(기본)에서 아동 계정으로 권한 변경",
        description = "아동 추가 회원가입입니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "아동 추가 회원가입 성공"),
        }
    )
    @PostMapping("/signup")
    public ResponseEntity<Void> createChild(
        @Valid @RequestBody ChildCreateRequestDto request
    ) {
        childService.createChild(
            request.accountId(),
            request.cardNumber(),
            request.Do(),
            request.sigun(),
            request.gu()
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}