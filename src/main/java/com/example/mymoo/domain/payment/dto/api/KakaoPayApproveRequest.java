package com.example.mymoo.domain.payment.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoPayApproveRequest {
    private String tid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;
}
