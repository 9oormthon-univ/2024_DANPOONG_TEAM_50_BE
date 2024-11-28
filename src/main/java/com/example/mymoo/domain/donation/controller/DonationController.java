package com.example.mymoo.domain.donation.controller;

import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.dto.response.DonationResponseDto;
import com.example.mymoo.domain.donation.dto.response.DonatorRankingResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadAccountDonationListResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadDonationResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadStoreDonationListResponseDto;
import com.example.mymoo.domain.donation.service.DonationService;
import com.example.mymoo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    @Operation(
        summary = "[후원자] 음식점에 포인트 후원",
        description = "후원자 계정의 Access token을 header에 넣었는지 확인해주세요",
        responses = {
            @ApiResponse(responseCode = "201", description = "후원 성공"),
        }
    )
    @PostMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<DonationResponseDto> donate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("storeId") Long storeId,
        @Valid @RequestBody DonationRequestDto donationRequestDto
    ) {
        Long accountId = userDetails.getAccountId();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                donationService.createDonation(
                    accountId,
                    storeId,
                    donationRequestDto
                )
            );
    }

    @Operation(
        summary = "[후원자] 자신의 후원 랭킹 및 전체 랭킹 조회",
        description = "자신의 후원 랭킹, 다른 후원자들의 후원 랭킹을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
        }
    )
    @GetMapping("/rankings")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<DonatorRankingResponseDto> getDonationRanking(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PageableDefault(size = 10) @Parameter(hidden = true) Pageable pageable
    ) {
        Long accountId = userDetails.getAccountId();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(donationService.getDonatorsRanking(accountId, pageable));
    }

    @Operation(
        summary = "[공통] 음식점 후원 목록 전체 조회",
        description = "특정 음식점의 후원 목록을 조회합니다. 사용 가능한 후원들만 오래된 순으로 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
        }
    )
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ReadStoreDonationListResponseDto> getStoreDonationList(
        @PathVariable("storeId") Long storeId,
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.ASC) @Parameter(hidden = true) Pageable pageable
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(donationService.getStoreDonationList(storeId, pageable));
    }

    @Operation(
        summary = "[후원자] 자신의 후원 목록 전체 조회",
        description = "후원자 자신의 후원 목록을 조회합니다. 후원자 계정의 access token을 header에 넣었는지 확인해주세요.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
        }
    )
    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<ReadAccountDonationListResponseDto> getAccountDonationList(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) Integer limit,
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) @Parameter(hidden = true) Pageable pageable
    ) {
        Long accountId = userDetails.getAccountId();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(donationService.getAccountDonationList(accountId, limit, pageable));
    }

    @Operation(
        summary = "[후원자] 자신의 후원 상세 조회",
        description = "후원자 자신의 후원을 조회합니다. 후원자 계정의 Access token을 header에 넣었는지 확인해주세요",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
        }
    )
    @GetMapping("/{donationId}")
    @PreAuthorize("hasAuthority('DONATOR')")
    public ResponseEntity<ReadDonationResponseDto> getDonation(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("donationId") Long donationId
    ) {
        Long accountId = userDetails.getAccountId();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(donationService.getDonation(accountId, donationId));
    }
}
