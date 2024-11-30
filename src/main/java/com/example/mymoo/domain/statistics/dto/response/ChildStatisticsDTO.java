package com.example.mymoo.domain.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class ChildStatisticsDTO {

    private LocalDateTime createAt;
    private Long totalCount;
    private List<ChildStatisticsElement> childStatistics;

    public static ChildStatisticsDTO from(
            final List<ChildStatisticsElement> childStatistics
    ){
        return ChildStatisticsDTO.builder()
                .createAt(LocalDateTime.now())
                .totalCount((long) childStatistics.size())
                .childStatistics(childStatistics)
                .build();
    }
}
