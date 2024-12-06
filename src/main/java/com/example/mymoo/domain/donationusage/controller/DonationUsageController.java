package com.example.mymoo.domain.donationusage.controller;

import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;
import com.example.mymoo.domain.donationusage.dto.request.DonationUsageUpdateMessageRequestDto;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import com.example.mymoo.domain.donationusage.service.DonationUsageService;
import com.example.mymoo.domain.email.EmailClient;
import com.example.mymoo.domain.email.dto.EmailSendDTO;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/donation-usages")
@RequiredArgsConstructor
public class DonationUsageController {

    private final DonationUsageService donationUsageService;
    private final EmailClient emailClient;
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
        DonationUsage donationUsage =  donationUsageService.useDonation(storeAccountId, donationUsageCreateRequestDto);
        // 후원 완료 후 알림 메일 전송
        emailClient.sendDonationUsageMail(EmailSendDTO.from(donationUsage));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @Operation(
        summary = "[아동] 감사의 말 작성",
        description = "QR 코드 인식 후 아동 계정에서 감사 메시지 작성 시 해당 API를 호출합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "작성 성공"),
        }
    )
    @PatchMapping("/")
    @PreAuthorize("hasAuthority('CHILD')")
    public ResponseEntity<Void> updateMessage(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody DonationUsageUpdateMessageRequestDto donationUsageUpdateMessageRequestDto
    ){
        Long childAccountId = userDetails.getAccountId();
        donationUsageService.updateMessage(childAccountId, donationUsageUpdateMessageRequestDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
