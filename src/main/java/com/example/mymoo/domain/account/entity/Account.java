package com.example.mymoo.domain.account.entity;

import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import com.example.mymoo.global.entity.BaseEntity;
import com.example.mymoo.global.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_ACCOUNT")
@Entity
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Size(min = 2, max = 10)
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImageUrl;

    @Size(min = 10, max = 11)
    @Column(nullable = true) // kakao 로그인 시 못넣음
    private String phoneNumber;

    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    @Column(nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // 기본 : DONATOR(인증 완료 시 수정)
    private UserRole role;

    @Builder
    public Account(
        final String email,
        final String password,
        final String nickname,
        final String profileImageUrl,
        final String phoneNumber,
        final Long point,
        final UserRole role
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        this.point = point;
        this.role = role;
    }

    public void chargePoint(Long chargingPoint){
        if (chargingPoint <= 0) {
            throw new IllegalArgumentException("충전하는 포인트는 0 이하일 수 없습니다.");
        }
        this.point += chargingPoint;
    }

    public void usePoint(Long usingPoint) {
        if (usingPoint <= 0) {
            throw new IllegalArgumentException("사용하는 포인트는 0 이하일 수 없습니다.");
        }
        if (this.point < usingPoint){
            throw new AccountException(AccountExceptionDetails.NOT_ENOUGH_POINTS);
        }
        this.point -= usingPoint;
    }

    public void changeUserRoleTo(UserRole userRole){
        if (Set.of("DONATOR", "CHILD").contains(this.role.getAuthority())){
            this.role = userRole;
        }
        else{
            throw new AccountException(AccountExceptionDetails.NOT_ABLE_TO_CHANGE_ROLE);
        }
    }

}