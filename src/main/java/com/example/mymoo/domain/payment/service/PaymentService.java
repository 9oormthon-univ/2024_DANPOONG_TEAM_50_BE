package com.example.mymoo.domain.payment.service;

import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyResponse;
import com.example.mymoo.domain.payment.dto.response.PayResponseDTO;

public interface PaymentService {
    KakaoPayReadyResponse payReady(String name, Integer totalPrice, Long accountId);
    PayResponseDTO approve(String pgToken, String tid, Long accountId);
}
