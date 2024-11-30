package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsElement;
import com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsElement;
import com.example.mymoo.domain.store.entity.Store;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByNameContainsOrAddressContains(String nameKeyword, String addressKeyword, Pageable pageable);
    List<Store> findAllByAccount_Id(Long storeAccountId);

}
