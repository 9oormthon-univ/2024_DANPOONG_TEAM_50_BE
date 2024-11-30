package com.example.mymoo.domain.donation.repository;

import com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto;
import com.example.mymoo.domain.donation.entity.Donation;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomDonationRepository {
    Slice<Donation> findRecentDonationsByAccountId(
        Long accountId,
        LocalDateTime startDate,
        Pageable pageable
    );

    Slice<DonatorTotalDonationRepositoryDto> findDonatorRankings(Pageable pageable);

    Optional<DonatorTotalDonationRepositoryDto> findTotalDonationByAccountId(Long accountId);

    Long findRankByAccountId(Long accountId);
}
