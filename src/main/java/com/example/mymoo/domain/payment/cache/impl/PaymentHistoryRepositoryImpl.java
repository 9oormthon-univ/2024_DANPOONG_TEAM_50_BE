package com.example.mymoo.domain.payment.cache.impl;

import com.example.mymoo.domain.payment.cache.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final String PAYMENT_HISTORY_PREFIX = "pay:";
    private final long PAYMENT_HISTORY_VALIDITY = 24*60*60*1000L; // 24시간

    @Override
    public void savePaymentHistory(String pgToken) {
        String key = PAYMENT_HISTORY_PREFIX + pgToken;
        redisTemplate.opsForValue().set(key, pgToken, PAYMENT_HISTORY_VALIDITY, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean findPaymentHistory(String pgToken) {
        String key = PAYMENT_HISTORY_PREFIX + pgToken;
        String token = redisTemplate.opsForValue().get(key);
        return token != null;
    }

}
