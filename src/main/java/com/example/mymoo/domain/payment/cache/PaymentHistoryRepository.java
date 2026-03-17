package com.example.mymoo.domain.payment.cache;

public interface PaymentHistoryRepository {
    void savePaymentHistory(String pgToken);
    boolean findPaymentHistory(String pgToken);
}
