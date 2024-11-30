package com.example.mymoo.domain.statistics.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class UtilizationStatisticsElement {
    private String address;
    private Long allDonation;
    private Long usableDonation;
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
        this.allDonation = allDonation;
        this.usableDonation = usableDonation;
        if (allDonation != null && allDonation > 0) {
            this.utilization = BigDecimal.valueOf(
                    usableDonation != null ?
                            (allDonation.doubleValue()-usableDonation.doubleValue())*100 / allDonation.doubleValue() : 0.0)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {
            this.utilization = 0.0;
        }
    }
}
