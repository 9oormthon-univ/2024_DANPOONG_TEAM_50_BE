package com.example.mymoo.domain.statistics.controller;

import com.example.mymoo.domain.statistics.dto.response.ChildStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.StoreStatisticsDTO;
import com.example.mymoo.domain.statistics.dto.response.UtilizationStatisticsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    @Operation(
            summary = "[톻계] 구 별 아동 수 통계",
            description = "구 별로 거주하는 결식아동의 수를 나타냅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "아동 수 통계 조회 성공"),
            }
    )
    @GetMapping("chlidren")
    public ResponseEntity<ChildStatisticsDTO> getChildStatistics(
    ){
        return null;
    }

    @Operation(
            summary = "[톻계] 구 별 후원금 사용률 통계",
            description = "구 별로 후원금 사용률을 (전체 사용 후원금)/(전체 누적 후원금) 나타냅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "후원금 사용률 통계 조회 성공"),
            }
    )
    @GetMapping("utilizations")
    public ResponseEntity<UtilizationStatisticsDTO> getUtilizationStatistics(
    ){
        return null;
    }

    @Operation(
            summary = "[톻계] 구 별 급식카드 가맹점 수 통계",
            description = "구 별로 영업 중인 급식카드 가맹점 수를 나타냅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "급식카드 가맹점 수 통계 조회 성공"),
            }
    )
    @GetMapping("stores")
    public ResponseEntity<StoreStatisticsDTO> getStoreStatistics(
    ){
        return null;
    }

}
