package com.example.mymoo.domain.payment.dto.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoPayApproveRequest {
    private String tid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;
}
