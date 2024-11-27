package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.store.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
}
