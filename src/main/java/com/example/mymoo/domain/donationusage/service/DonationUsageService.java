package com.example.mymoo.domain.donationusage.service;

import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;

public interface DonationUsageService {
    void useDonation(
        Long storeAccountId,
        DonationUsageCreateRequestDto donationUsageCreateRequestDto
    );
}
