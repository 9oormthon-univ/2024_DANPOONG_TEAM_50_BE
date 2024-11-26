package com.example.mymoo.domain.payment.service.Impl;

import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyRequest;
import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyResponse;
import com.example.mymoo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WebClient webClient;

    @Value("${kakao.pay.secret-key}")
    private String secretKey;
    @Value("${kakao.pay.uri}")
    private String uri;
    @Value("${kakao.pay.cid}")
    private String cid;

    public KakaoPayReadyResponse payReady(String name, Integer totalPrice){

        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        KakaoPayReadyRequest readyRequest = KakaoPayReadyRequest.builder()
                .cid(cid)
                .partnerOrderId("1")
                .partnerUserId("test")
                .itemName(name)
                .quantity(1)
                .totalAmount(totalPrice)
                .taxFreeAmount(0)
                .vatAmount(100)
                .approvalUrl("http://localhost:8081/pay/approve")
                .cancelUrl("http://localhost:8081/pay/cancel")
                .failUrl("http://localhost:8081/pay/fail")
                .build();

        // Send reqeust
        HttpEntity<KakaoPayReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<KakaoPayReadyResponse> response = new RestTemplate().postForEntity(
                uri,
                entityMap,
                KakaoPayReadyResponse.class
        );

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        return response.getBody();
    }
}
