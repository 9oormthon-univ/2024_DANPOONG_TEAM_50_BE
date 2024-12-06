package com.example.mymoo.domain.donationusage.service;

import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;
import com.example.mymoo.domain.donationusage.dto.request.DonationUsageUpdateMessageRequestDto;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;

public interface DonationUsageService {
    DonationUsage useDonation(
        Long storeAccountId,
        DonationUsageCreateRequestDto donationUsageCreateRequestDto
    );

    void updateMessage(
        Long childAccountId,
        DonationUsageUpdateMessageRequestDto donationUsageUpdateMessageRequestDto
    );
}
