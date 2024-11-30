package com.example.mymoo.domain.statistics.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class UtilizationStatisticsElement {
    private String address;
    private Long allDonation;
    private Long usedDonation;
    private Double utilization;

    public UtilizationStatisticsElement(
            final String doName,
            final String sigun,
            final String guName,
            final Long allDonation,
            final Long usableDonation
    ) {
        this.address = doName+" "+sigun;
        if (guName != null){
            this.address += " "+guName;
        }
        this.allDonation = allDonation != null ? allDonation : 0L;
        this.usedDonation = this.allDonation - (usableDonation != null ? usableDonation : 0L);
        if (this.allDonation > 0) {
            this.utilization = BigDecimal.valueOf(
                this.usedDonation*100 / this.allDonation
            ).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {
            this.utilization = 0.0;
        }
    }
}
