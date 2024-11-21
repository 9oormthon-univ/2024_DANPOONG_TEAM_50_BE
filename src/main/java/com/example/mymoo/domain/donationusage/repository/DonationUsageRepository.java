package com.example.mymoo.domain.donationusage.repository;

import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationUsageRepository extends JpaRepository<DonationUsage, Long> {
    Optional<DonationUsage> findByDonation_id(Long donationId);
}
