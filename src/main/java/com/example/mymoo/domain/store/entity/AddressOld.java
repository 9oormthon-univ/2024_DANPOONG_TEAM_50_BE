package com.example.mymoo.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_ADDERESS_OLD")
@Entity
public class AddressOld {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "do", nullable = false, columnDefinition = "char(10)")
    private String Do;

    @Column(name="sigun", nullable = false, columnDefinition = "char(10)")
    private String sigun;

    @Column(name="gu", columnDefinition = "char(10)")
    private String gu;

    @Column(name="dong", nullable = false, columnDefinition = "char(20)")
    private String dongUepmean;

    @Column(name="detail", nullable = false, columnDefinition = "char(100)")
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public AddressOld(String Do, String sigun, String gu, String dongUepmean, String detail, Store store) {
        this.Do = Do;
        this.sigun = sigun;
        this.gu = gu;
        this.dongUepmean = dongUepmean;
        this.detail = detail;
        this.store = store;
    }


    public static AddressOld convertToAddress(String address, Store store) {

        String[] slicedString = address.split(" ");
        if (address.isBlank()){
            return AddressOld.builder()
                    .Do("")
                    .sigun("")
                    .gu("")
                    .dongUepmean("")
                    .detail("")
                    .store(store)
                    .build();
        }
        String gu,dong,detail;
        int index;
        if (slicedString[6].endsWith("구")) {
            gu = slicedString[6];
            dong = slicedString[7];
            index = 8;
        }else{
            gu = null;
            dong = slicedString[6];
            index = 7;
        }

        detail = Arrays.stream(slicedString, index, slicedString.length)
                .collect(Collectors.joining());

        return AddressOld.builder()
                .Do(slicedString[4])
                .sigun(slicedString[5])
                .gu(gu)
                .dongUepmean(dong)
                .detail(detail)
                .store(store)
                .build();
    }
}
