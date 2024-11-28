package com.example.mymoo.domain.payment.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.payment.dto.api.KakaoPayApproveRequest;
import com.example.mymoo.domain.payment.dto.api.KakaoPayApproveResponse;
import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyRequest;
import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyResponse;
import com.example.mymoo.domain.payment.dto.response.PayResponseDTO;
import com.example.mymoo.domain.payment.exception.PaymentException;
import com.example.mymoo.domain.payment.exception.PaymentExceptionDetails;
import com.example.mymoo.domain.payment.service.PaymentService;
import com.example.mymoo.global.aop.LogExecutionTime;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Service @LogExecutionTime
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;

    @Value("${kakao.pay.secret-key}")
    private String secretKey;
    @Value("${kakao.pay.uri}")
    private String uri;
    @Value("${kakao.pay.cid}")
    private String cid;
    @Value("${kakao.pay.approve-Url}")
    private String approvalUrl;
    @Value("${kakao.pay.partner-order-id}")
    private String partnerOrderId;

    private String tid;

    public KakaoPayReadyResponse payReady(String name, Integer totalPrice, Long accountId){
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        KakaoPayReadyRequest readyRequest = KakaoPayReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(String.valueOf(accountId))
                .itemName(name)
                .quantity(1)
                .totalAmount(totalPrice)
                .taxFreeAmount(0)
                .vatAmount(100)
                .approvalUrl(approvalUrl)
                .cancelUrl("http://localhost:8080/payment/cancel")
                .failUrl("http://localhost:8080/payment/fail")
                .build();

        // Send reqeust
        HttpEntity<KakaoPayReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<KakaoPayReadyResponse> response = new RestTemplate().postForEntity(
                uri,
                entityMap,
                KakaoPayReadyResponse.class
        );
        this.tid = response.getBody().getTid();
        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        return response.getBody();
    }

    @Transactional
    public PayResponseDTO approve(String pgToken, String tida, Long accountId){
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Request param
        KakaoPayApproveRequest approveRequest = KakaoPayApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(String.valueOf(accountId))
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<KakaoPayApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<KakaoPayApproveResponse> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    KakaoPayApproveResponse.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
            KakaoPayApproveResponse res = response.getBody();
            // account 계정에 결제금액 만큼 포인트 충전
            Account foundAccount = accountRepository.findById(Long.valueOf(res.getPartner_user_id()))
                    .orElseThrow(() -> new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));
            System.out.println(Long.valueOf(res.getAmount().getTotal()));
            foundAccount.chargePoint(Long.valueOf(res.getAmount().getTotal()));

            return PayResponseDTO.builder()
                    .item_name(res.getItem_name())
                    .account_name(foundAccount.getNickname())
                    .total(res.getAmount().getTotal())
                    .created_at(res.getCreated_at())
                    .approved_at(res.getApproved_at())
                    .build();

        } catch (HttpStatusCodeException ex) {
            throw new PaymentException(PaymentExceptionDetails.APPROVE_FAILED);
        }
    }
}
