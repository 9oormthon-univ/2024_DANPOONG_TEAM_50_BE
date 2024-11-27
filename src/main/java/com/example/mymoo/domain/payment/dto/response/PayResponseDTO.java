package com.example.mymoo.domain.payment.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PayResponseDTO {
    private String item_name;
    private String account_name;
    private int total;
    private String created_at; // 결제 요청 시간
    private String approved_at; // 결제 승인 시간
}
