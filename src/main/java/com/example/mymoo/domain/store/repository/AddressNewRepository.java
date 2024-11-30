package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.statistics.dto.response.StoreStatisticsElement;
import com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsElement;
import com.example.mymoo.domain.store.entity.AddressNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressNewRepository extends JpaRepository<AddressNew, Long> {

    @Query("SELECT new com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsElement(" +
            "a.Do, a.sigun, a.gu, " +
            "SUM(s.allDonation), SUM(s.usableDonation) " +
            ") " +
            "FROM AddressNew a " +
            "JOIN Store s ON s.id = a.id " +
            "WHERE a.Do != ' ' " +
            "GROUP BY a.Do, a.sigun, a.gu")
    List<UtilizationStatisticsElement> findDonationInfoByDoAndSigunAndGu();

    @Query(value = """
            SELECT new com.example.mymoo.domain.statistics.dto.response.StoreStatisticsElement(a.Do, a.sigun, a.gu, COUNT(a)) 
            FROM AddressNew a 
            WHERE a.Do != ' '  
            GROUP BY a.Do, a.sigun, a.gu
            ORDER BY COUNT(a) DESC"""
    )
    List<StoreStatisticsElement> countAddressNewByDoAndSigunAndGu();
}
