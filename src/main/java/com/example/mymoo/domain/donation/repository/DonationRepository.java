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

public interface DonationRepository extends JpaRepository<Donation, Long> {
    Slice<Donation> findAllByStore_IdAndIsUsedFalse(
        Long storeId,
        Pageable pageable
    );

    Slice<Donation> findAllByAccount_Id(
        Long accountId,
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

    @Query("SELECT new com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto(" +
        "d.account.id, d.account.nickname, d.account.profileImageUrl, SUM(d.point), MAX(d.createdAt)) " +
        "FROM Donation d " +
        "GROUP BY d.account.id, d.account.nickname, d.account.profileImageUrl " +
        "ORDER BY SUM(d.point) DESC, MAX(d.createdAt) DESC, d.account.id ASC")
    Slice<DonatorTotalDonationRepositoryDto> findDonatorRankings(Pageable pageable);

    @Query("SELECT new com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto(" +
        "d.account.id, d.account.nickname, d.account.profileImageUrl, SUM(d.point), MAX(d.createdAt)) " +
        "FROM Donation d " +
        "WHERE d.account.id = :accountId")
    Optional<DonatorTotalDonationRepositoryDto> findTotalDonationByAccountId(@Param("accountId") Long accountId);


    // 자신의 총 point보다 큰 계정 OR 총 point가 같고 자신보다 최근에 후원한 계정의 수 + 1 반환
    @Query("SELECT COUNT(DISTINCT d2.account.id) + 1 FROM Donation d2 " +
        "WHERE (" +
        "    (SELECT SUM(d.point) FROM Donation d WHERE d.account.id = :accountId) < " +
        "    (SELECT SUM(d3.point) FROM Donation d3 WHERE d3.account.id = d2.account.id) " +
            "OR (" +
            "    (SELECT SUM(d.point) FROM Donation d WHERE d.account.id = :accountId) = " +
            "    (SELECT SUM(d3.point) FROM Donation d3 WHERE d3.account.id = d2.account.id) AND " +
            "    (SELECT MAX(d.createdAt) FROM Donation d WHERE d.account.id = :accountId) < " +
            "    (SELECT MAX(d4.createdAt) FROM Donation d4 WHERE d4.account.id = d2.account.id)" +
            ")" +
        ")"
    )
    Long findRankByAccountId(@Param("accountId") Long accountId);
}
