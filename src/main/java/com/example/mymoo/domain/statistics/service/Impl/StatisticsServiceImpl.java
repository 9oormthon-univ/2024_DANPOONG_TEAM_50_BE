package com.example.mymoo.domain.statistics.service.Impl;

import com.example.mymoo.domain.child.repository.LocationRepository;
import com.example.mymoo.domain.statistics.dto.response.*;
import com.example.mymoo.domain.statistics.service.StatisticsService;
import com.example.mymoo.domain.store.repository.AddressNewRepository;
import com.example.mymoo.global.aop.log.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) @LogExecutionTime
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AddressNewRepository addressNewRepository;
    private final LocationRepository locationRepository;

    // 도 시/군 구 별 결식아동 현황
    public ChildStatisticsDTO getChildStatistics(){

        List<ChildStatisticsElement> childStatistics
                = locationRepository.countLocationsByDoAndSigunAndGu();

        return ChildStatisticsDTO.from(childStatistics);
    }

    // 도 시/군 구 별 후원금액 사용율 현황
    public UtilizationStatisticsDTO getUtilizationStatistics(){

        List<UtilizationStatisticsElement> utilizationStatistics
                = addressNewRepository.findDonationInfoByDoAndSigunAndGu();

        return UtilizationStatisticsDTO.from(utilizationStatistics);
    }

    // 도 시/군 구 별 급식카드 가맹점 현황
    public StoreStatisticsDTO getStoreStatistics(){
        List<StoreStatisticsElement> storeStatistics
                = addressNewRepository.countAddressNewByDoAndSigunAndGu();

        return StoreStatisticsDTO.from(storeStatistics);
    }
}
