package com.example.mymoo.domain.payment.controller;

import com.example.mymoo.domain.payment.dto.api.KakaoPayReadyResponse;
import com.example.mymoo.domain.payment.dto.request.PayRequestDTO;
import com.example.mymoo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("ready")
    public ResponseEntity<KakaoPayReadyResponse> payReady(
            @RequestBody PayRequestDTO req
    ){
        KakaoPayReadyResponse res = paymentService.payReady(req.getName(), req.getTotalPrice());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @GetMapping("approve")
    public String approve() {
        //승인 처리 - 이 부분은 프론트에서 1차적으로 리다이렉트
        // 프론트에서 받은 결제 정보를 해당 api에 넘겨주면 서버에 반영됨
        return "approve";
    }

    @GetMapping("cancel")
    public String cancel() {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        return "cancel";
    }

    @GetMapping("fail")
    public String fail() {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        return "fail";
    }
}
