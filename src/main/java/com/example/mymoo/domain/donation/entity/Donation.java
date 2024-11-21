package com.example.mymoo.domain.donation.entity;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.donation.exception.DonationException;
import com.example.mymoo.domain.donation.exception.DonationExceptionDetails;
import com.example.mymoo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "TB_DONATION")
@Entity
public class Donation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "후원 금액은 0 이상이어야 합니다.")
    @Column(nullable = false)
    private Long point = 0L;

    @Column(nullable = false)
    private Boolean isUsed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "store_id", nullable = false)
    protected Store store;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "donator_id", nullable = false)
    protected Account account;

    @Builder
    public Donation(
        final Long point,
        final Boolean isUsed,
        final Store store,
        final Account account
    ) {
        this.point = point;
        this.isUsed = isUsed;
        this.store = store;
        this.account = account;
    }

    public void setIsUsedToTrue() {
        // 이미 사용된 후원을 사용하려 할 때
        if (this.isUsed){
            throw new DonationException(DonationExceptionDetails.DONATION_ALREADY_USED);
        }
        this.isUsed = true;
    }
}
