package com.example.mymoo.domain.statistics.dto.response;

import lombok.Data;

@Data
public class ChildStatisticsElement{
    private String address;
    private Long childrenCount;

    public ChildStatisticsElement(String doName, String sigun, String guName, Long childrenCount) {
        this.address = doName+" "+sigun;
        if (guName != null){
            this.address += " "+guName;
        }
        this.childrenCount = childrenCount;
    }
}