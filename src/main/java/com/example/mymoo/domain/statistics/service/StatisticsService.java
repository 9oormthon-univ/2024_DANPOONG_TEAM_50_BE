package com.example.mymoo.domain.statistics.service;

import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.StoreStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsDTO;

public interface StatisticsService {
    ChildStatisticsDTO getChildStatistics();
    UtilizationStatisticsDTO getUtilizationStatistics();
    StoreStatisticsDTO getStoreStatistics();
}
