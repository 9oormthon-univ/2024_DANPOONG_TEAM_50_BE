package com.example.mymoo.domain.statistics.service.Impl;

import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.StoreStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsDTO;
import com.example.mymoo.domain.statistics.service.StatisticsService;
import com.example.mymoo.global.aop.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional @LogExecutionTime
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    public ChildStatisticsDTO getChildStatistics(){
        return null;
    }

    public UtilizationStatisticsDTO getUtilizationStatistics(){
        return null;
    }

    public StoreStatisticsDTO getStoreStatistics(){
        return null;
    }
}
