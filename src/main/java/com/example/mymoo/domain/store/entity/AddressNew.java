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
@Table(name = "TB_ADDERESS_NEW")
@Entity
public class AddressNew {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "do", nullable = false, columnDefinition = "char(10)")
    private String Do;

    @Column(name="sigun", nullable = false, columnDefinition = "char(10)")
    private String sigun;

    @Column(name="gu",  columnDefinition = "char(10)")
    private String gu;

    @Column(name="dong", columnDefinition = "char(100)")
    private String dongUepmean;

    @Column(name="ro", nullable = false, columnDefinition = "char(50)")
    private String ro;

    @Column(name="detail", nullable = false, columnDefinition = "char(100)")
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder()
    public AddressNew(String Do, String sigun, String gu, String dongUepmean, String ro, String detail, Store store) {
        this.Do = Do;
        this.sigun = sigun;
        this.gu = gu;
        this.dongUepmean = dongUepmean;
        this.ro = ro;
        this.detail = detail;
        this.store = store;
    }

    @Override
    public String toString(){
        return Do+" "+sigun+" "+gu+" "+dongUepmean+" "+ro+" "+detail;
    }


    public static AddressNew convertToAddress(String address, Store store) {

        if (address.isBlank()){
            return AddressNew.builder()
                    .Do("")
                    .sigun("")
                    .gu("")
                    .dongUepmean("")
                    .ro("")
                    .detail("")
                    .store(store)
                    .build();
        }

        String[] slicedString = address.split(" ");

        if (slicedString.length <= 8){
            return AddressNew.builder()
                    .Do("")
                    .sigun("")
                    .gu("")
                    .dongUepmean("")
                    .ro("")
                    .detail("")
                    .store(store)
                    .build();
        }

        String gu,dong,ro,detail;

        int index;
        if (slicedString[6].endsWith("구")) {
            gu = slicedString[6];
            index = 7;
        }else{
            gu = null;
            index = 6;
        }

        if (slicedString[index].endsWith("로") || slicedString[index].endsWith("길")){
            dong = null;
            ro = slicedString[index];
            index += 1;
        }else{
            dong = slicedString[index];
            ro = slicedString[++index];
            index += 1;
        }


        detail = Arrays.stream(slicedString, index, slicedString.length)
                 .collect(Collectors.joining());


        return AddressNew.builder()
                .Do(slicedString[4])
                .sigun(slicedString[5])
                .gu(gu)
                .dongUepmean(dong)
                .ro(ro)
                .detail(detail)
                .store(store)
                .build();

    }
}
