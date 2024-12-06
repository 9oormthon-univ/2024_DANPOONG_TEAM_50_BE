package com.example.mymoo.domain.donation.repository;

import com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto;
import com.example.mymoo.domain.donation.entity.Donation;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonationRepository extends JpaRepository<Donation, Long>, CustomDonationRepository {
    Slice<Donation> findAllByStore_IdAndIsUsedFalse(
        Long storeId,
        Pageable pageable
    );

    Slice<Donation> findAllByAccount_Id(
        Long accountId,
        Pageable pageable
    );

    Boolean existsByAccount_Id(Long accountId);
}
