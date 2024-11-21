package com.example.mymoo.domain.store.dto.api;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "row")
public class Row{
    private String name;
    private String zipcode;
    private String adderssOld;
    private String addressNew;
    private Double LOGT;
    private Double LAT;

    @Builder
    public Row(String name, String zipcode, String adderssOld, String addressNew, Double LOGT, Double LAT) {
        this.name = name;
        this.zipcode = zipcode;
        this.adderssOld = adderssOld;
        this.addressNew = addressNew;
        this.LOGT = LOGT;
        this.LAT = LAT;
    }
}
