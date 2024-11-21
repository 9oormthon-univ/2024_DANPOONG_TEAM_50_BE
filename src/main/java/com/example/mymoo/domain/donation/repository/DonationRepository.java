package com.example.mymoo.domain.donation.repository;

import com.example.mymoo.domain.donation.entity.Donation;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    Slice<Donation> findAllByStore_IdAndIsUsedFalse(
        Long storeId,
        Pageable pageable
    );

    @Query("SELECT d FROM Donation d WHERE d.account.id = :accountId " +
        "AND d.createdAt >= :startDate " +
        "ORDER BY d.createdAt DESC")
    Slice<Donation> findRecentDonationsByAccountId(
        @Param("accountId") Long accountId,
        @Param("startDate") LocalDateTime startDate,
        Pageable pageable
    );

    Slice<Donation> findAllByAccount_Id(
        Long accountId,
        Pageable pageable
    );
}
