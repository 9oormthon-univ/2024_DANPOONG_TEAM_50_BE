package com.example.mymoo.global.security.jwt;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.global.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long ACCESS_TOKEN_VALIDITY = 1*30*60*1000L; // 30분

    public static final long REFRESH_TOKEN_VALIDITY = 24*60*60*1000L; // 24시간

    public static final String AUTHORIZATION_KEY = "auth";
    public static final String USER_ID_KEY = "userId";

    public String createAccessToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return createToken(
            customUserDetails.getAccountId(),
            customUserDetails.getAuthorities(),
            ACCESS_TOKEN_VALIDITY
        );
    }

    public String createAccessTokenWithRefreshTokenInfo(
        Long accountId,
        String authority
    ) {
        return createToken(
            accountId,
            Collections.singletonList(
                new SimpleGrantedAuthority(authority)
            ),
            ACCESS_TOKEN_VALIDITY
        );
    }

    public String createAccessTokenWithAccountEntity(Account account) {
        return createToken(
            account.getId(),
            Collections.singletonList(
                new SimpleGrantedAuthority(String.valueOf(account.getRole()))
            ),
            ACCESS_TOKEN_VALIDITY
        );
    }

    public String createRefreshToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return createToken(
            customUserDetails.getAccountId(),
            customUserDetails.getAuthorities(),
            REFRESH_TOKEN_VALIDITY
        );
    }

    public String createRefreshTokenWithAccountEntity(Account account) {
        return createToken(
            account.getId(),
            Collections.singletonList(
                new SimpleGrantedAuthority(String.valueOf(account.getRole()))
            ),
            REFRESH_TOKEN_VALIDITY
        );
    }

    private String createToken(
        Long accountId,
        Collection<? extends GrantedAuthority> authorities,
        long validityInMilliseconds
    ) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(validity)
            .claim(USER_ID_KEY, accountId)
            .claim(AUTHORIZATION_KEY, authorities.iterator().next().getAuthority())
            .signWith(key)
            .compact();
    }

    public Claims getClaimsFromToken(String token) throws JwtException {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty", e);
        }
    }

    // jwt의 claims로부터 인증 완료된 객체 반환(컨트롤러에서 사용)
    public Authentication getAuthenticationFromClaims(Claims claims) {
        Long accountId = claims.get(USER_ID_KEY,Long.class);
        String authority = claims.get(AUTHORIZATION_KEY, String.class);

        UserDetails userDetails = CustomUserDetails.builder()
            .accountId(accountId) // accountId만 이용
            .email(null)
            .password(null)
            .authorities(
                Collections.singletonList(
                    new SimpleGrantedAuthority(authority)
                )
            )
            .build();

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null, // pw 쓸 일 없음
            userDetails.getAuthorities()
        );
    }
}