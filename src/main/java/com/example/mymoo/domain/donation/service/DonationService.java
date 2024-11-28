package com.example.mymoo.domain.donation.service;

import com.example.mymoo.domain.donation.dto.request.DonationRequestDto;
import com.example.mymoo.domain.donation.dto.response.DonationResponseDto;
import com.example.mymoo.domain.donation.dto.response.DonatorRankingResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadAccountDonationListResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadDonationResponseDto;
import com.example.mymoo.domain.donation.dto.response.ReadStoreDonationListResponseDto;
import org.springframework.data.domain.Pageable;

public interface DonationService {
    DonationResponseDto createDonation(
        final Long accountId,
        final Long storeId,
        final DonationRequestDto donationRequestDto
    );

    ReadStoreDonationListResponseDto getStoreDonationList(
        final Long storeId,
        final Pageable pageable
    );

    ReadAccountDonationListResponseDto getAccountDonationList(
        final Long accountId,
        final Integer limit,
        final Pageable pageable
    );
    ReadDonationResponseDto getDonation(
        final Long accountId,
        final Long donationId
    );
    DonatorRankingResponseDto getDonatorsRanking(
        final Long accountId,
        final Pageable pageable
    );
}
