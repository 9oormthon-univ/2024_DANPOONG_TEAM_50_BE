package com.example.mymoo.domain.child.repository;

import com.example.mymoo.domain.child.entity.Location;
import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = """
            SELECT new com.example.mymoo.domain.statistics.dto.response.ChildStatisticsElement(l.Do, l.sigun, l.gu, COUNT(l)) 
            FROM Location l 
            WHERE l.Do != ' ' 
            GROUP BY l.Do, l.sigun, l.gu
            ORDER BY COUNT(l) DESC"""
    )
    List<ChildStatisticsElement> countLocationsByDoAndSigunAndGu();
}
