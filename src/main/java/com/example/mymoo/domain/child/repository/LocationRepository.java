package com.example.mymoo.domain.child.repository;

import com.example.mymoo.domain.child.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
