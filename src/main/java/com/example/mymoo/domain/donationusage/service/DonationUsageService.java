package com.example.mymoo.domain.donationusage.service;

import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;
import com.example.mymoo.domain.donationusage.dto.request.DonationUsageUpdateMessageRequestDto;

public interface DonationUsageService {
    void useDonation(
        Long storeAccountId,
        DonationUsageCreateRequestDto donationUsageCreateRequestDto
    );

    void updateMessage(
        Long childAccountId,
        DonationUsageUpdateMessageRequestDto donationUsageUpdateMessageRequestDto
    );
}
