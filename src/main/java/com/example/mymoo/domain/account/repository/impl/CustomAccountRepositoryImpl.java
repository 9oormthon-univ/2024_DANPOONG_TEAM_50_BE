package com.example.mymoo.domain.account.repository.impl;

import static com.example.mymoo.domain.account.entity.QAccount.account;

import com.example.mymoo.domain.account.entity.QAccount;
import com.example.mymoo.domain.account.repository.CustomAccountRepository;
import com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donation.entity.QDonation;
import com.example.mymoo.domain.donation.repository.CustomDonationRepository;
import com.example.mymoo.global.enums.UserRole;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 기부하지 않은 DONATOR 계정들을 회원가입 먼저 한 순서대로 정렬해서 반환
     */
    @Override
    public List<DonatorTotalDonationRepositoryDto> findAllNotDonatedDonatorsWithSort(final List<Long> donatedAccountIds) {
        return queryFactory
            .select(Projections.constructor(
                DonatorTotalDonationRepositoryDto.class,
                account.id,
                account.nickname,
                account.profileImageUrl,
                Expressions.asNumber(0L),  // totalDonation
                Expressions.nullExpression(LocalDateTime.class)  // lastDonatedAt
            ))
            .from(account)
            .where(
                account.role.eq(UserRole.DONATOR)
                    .and(account.id.notIn(donatedAccountIds))
            )
            .orderBy(account.createdAt.desc(), account.id.asc())
            .fetch();
    }
}
