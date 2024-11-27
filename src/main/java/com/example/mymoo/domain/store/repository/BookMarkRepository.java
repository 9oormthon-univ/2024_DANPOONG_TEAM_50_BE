package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.store.entity.BookMark;
import com.example.mymoo.domain.store.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findByAccount_IdAndStore_Id(
            final Long accountId,
            final Long storeId
    );

    Page<BookMark> findAllByAccount_Id(
            final Long accountId,
            final Pageable pageable
    );
}
