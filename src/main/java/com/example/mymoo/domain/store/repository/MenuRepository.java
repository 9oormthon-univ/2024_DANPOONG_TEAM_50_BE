package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.store.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
