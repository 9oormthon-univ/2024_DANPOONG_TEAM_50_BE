package com.example.mymoo.domain.store.entity;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_STORE")
@Entity
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "방문 횟수는 0 이상이어야 합니다.")
    @Column(nullable = false)
    private Integer visitCount = 0;

    @Min(value = 0, message = "이용 가능 금액은 0 이상이어야 합니다.")
    @Column(nullable = false)
    private Long usableDonation = 0L;

    // TODO - 위치 등 음식점 정보 추가

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Store(
        final Integer visitCount,
        final Long usableDonation,
        final Account account
    ) {
        this.visitCount = visitCount;
        this.usableDonation = usableDonation;
        this.account = account;
    }

    public void incrementVisitCount() {
        this.visitCount++;
    }

    public void addUsableDonation(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("후원하는 금액은 0 이하일 수 없습니다.");
        }
        this.usableDonation += amount;
    }

    public void useUsableDonation(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0 이하일 수 없습니다.");
        }
        if (this.usableDonation < amount) {
            // TODO - custom exception 이용
            // throw new IllegalArgumentException("후원 금액이 부족합니다.");
        }
        this.usableDonation -= amount;
    }
}
