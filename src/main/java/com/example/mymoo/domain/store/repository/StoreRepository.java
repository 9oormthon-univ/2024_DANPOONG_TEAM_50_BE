package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByNameContainsOrAddressContains(String nameKeyword, String addressKeyword, Pageable pageable);
}
