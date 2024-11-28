package com.example.mymoo.domain.payment.dto.api;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KakaoPayReadyResponse {
    private String tid;
    private Boolean tms_result;
    private String created_at;
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
    private String next_redirect_app_url;
    private String android_app_scheme;
    private String ios_app_scheme;
}
