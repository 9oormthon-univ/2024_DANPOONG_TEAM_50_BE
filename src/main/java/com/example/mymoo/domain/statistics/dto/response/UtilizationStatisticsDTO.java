package com.example.mymoo.domain.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Data @Builder
public class UtilizationStatisticsDTO {
    private LocalDateTime createAt;
    private Long totalCount;
    private List<UtilizationStatisticsElement> utilizationStatistics;

    public static UtilizationStatisticsDTO from(
            final List<UtilizationStatisticsElement> utilizationStatistics
    ) {
        return UtilizationStatisticsDTO.builder()
                .createAt(LocalDateTime.now())
                .totalCount((long) utilizationStatistics.size())
                .utilizationStatistics(sorted(utilizationStatistics))
                .build();
    }

    // 후원 사용율이 높은 순으로 정렬
    private static List<UtilizationStatisticsElement> sorted(
            final List<UtilizationStatisticsElement> utilizationStatistics
    ) {
        Map<Double, UtilizationStatisticsElement> map = new HashMap<>();
        for(UtilizationStatisticsElement element : utilizationStatistics) {
            map.put(element.getUtilization(), element);
        }

        List<Double> keySet = new ArrayList<>(map.keySet());
        keySet.sort(Comparator.reverseOrder());
        List<UtilizationStatisticsElement> result = new ArrayList<>();
        for(Double key : keySet) {
            result.add(map.get(key));
        }
        return result;
    }
}
