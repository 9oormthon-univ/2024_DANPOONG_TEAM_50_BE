package com.example.mymoo.global.auth.repository;

import java.util.Optional;

public interface AuthRepository {
    void saveRefreshToken(
        Long userId,
        String refreshToken,
        long expirationTime
    );

    Optional<String> findRefreshTokenByAccountId(Long userId);

    void deleteRefreshToken(Long userId);
}
