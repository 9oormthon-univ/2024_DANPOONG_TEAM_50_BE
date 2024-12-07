package com.example.mymoo.domain.donation.repository;

import com.example.mymoo.domain.donation.entity.Donation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long>, CustomDonationRepository {
    Slice<Donation> findAllByStore_IdAndIsUsedFalse(
        Long storeId,
        Pageable pageable
    );

    Slice<Donation> findAllByAccount_Id(
        Long accountId,
        Pageable pageable
    );
}
