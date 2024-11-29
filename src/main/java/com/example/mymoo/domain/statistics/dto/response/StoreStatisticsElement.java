package com.example.mymoo.domain.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StoreStatisticsElement {
    private String address;
    private Long totalCount;

    public StoreStatisticsElement(String doName, String sigun, String guName, Long storeCount) {
        this.address = doName+" "+sigun;
        if (guName != null){
            this.address += " "+guName;
        }
        this.totalCount = storeCount;
    }
}
