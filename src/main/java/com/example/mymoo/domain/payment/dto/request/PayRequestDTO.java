package com.example.mymoo.domain.payment.dto.request;

import lombok.Data;

@Data
public class PayRequestDTO {
    private String name;
    private Integer totalPrice;
}
