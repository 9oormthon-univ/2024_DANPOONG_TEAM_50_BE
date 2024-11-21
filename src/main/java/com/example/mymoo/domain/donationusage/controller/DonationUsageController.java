package com.example.mymoo.domain.donationusage.controller;

import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;
import com.example.mymoo.domain.donationusage.service.DonationUsageService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/donation-usages")
@RequiredArgsConstructor
public class DonationUsageController {

    private final DonationUsageService donationUsageService;
    @Operation(
        summary = "[음식점] 후원 금액 사용 처리",
        description = "음식점 계정에서 QR 코드 인식 시 해당 API를 호출합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "사용 처리 성공"),
        }
    )
    @PostMapping("/")
    @PreAuthorize("hasAuthority('STORE')")
    public ResponseEntity<Void> useDonation(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody DonationUsageCreateRequestDto donationUsageCreateRequestDto
    ) {
        Long storeAccountId = userDetails.getAccountId();
        donationUsageService.useDonation(storeAccountId, donationUsageCreateRequestDto);
        // TODO - 알림 기능 추가
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
