package com.example.mymoo.domain.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class StoreStatisticsDTO {
    private LocalDateTime createAt;
    private Long totalCount;
    private List<StoreStatisticsElement> storeStatistics;

    public static StoreStatisticsDTO from(List<StoreStatisticsElement> storeStatistics){
        return StoreStatisticsDTO.builder()
                .createAt(LocalDateTime.now())
                .totalCount((long) storeStatistics.size())
                .storeStatistics(storeStatistics)
                .build();
    }
}
