package com.example.mymoo.domain.payment.service;

import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyResponse;

public interface PaymentService {
    KakaoPayReadyResponse payReady(String name, Integer totalPrice);
}
