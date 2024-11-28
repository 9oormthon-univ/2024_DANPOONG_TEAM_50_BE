package com.example.mymoo.domain.donationusage.repository;

import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationUsageRepository extends JpaRepository<DonationUsage, Long> {
    Optional<DonationUsage> findByDonation_id(Long donationId);

    long countByChildAndCreatedAtBetween(Child child, LocalDateTime start, LocalDateTime end);
}
