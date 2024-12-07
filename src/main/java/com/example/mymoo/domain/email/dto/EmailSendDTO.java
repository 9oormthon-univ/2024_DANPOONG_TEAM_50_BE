package com.example.mymoo.domain.email.dto;

import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class EmailSendDTO {
    private String email;
    private String storeName;
    private Long usedPrice;
    private String childName;
    private LocalDateTime usedTime;
    private LocalDateTime donatedTime;

    public static EmailSendDTO from(DonationUsage donationUsage){

        String email = donationUsage.getDonation().getAccount().getEmail();

        if (email.contains("_")){
            email = email.substring(email.lastIndexOf("_")+1);
        }

        return EmailSendDTO.builder()
                .email(email)
                .storeName(donationUsage.getDonation().getStore().getName())
                .usedPrice(donationUsage.getDonation().getPoint())
                .childName(donationUsage.getChild().getAccount().getNickname())
                .usedTime(donationUsage.getCreatedAt())
                .donatedTime(donationUsage.getDonation().getCreatedAt())
                .build();
    }
}
