package com.example.mymoo.domain.donation.repository.impl;

import com.example.mymoo.domain.account.entity.QAccount;
import com.example.mymoo.domain.donation.dto.response.DonatorTotalDonationRepositoryDto;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donation.entity.QDonation;
import com.example.mymoo.domain.donation.repository.CustomDonationRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CustomDonationRepositoryImpl implements CustomDonationRepository {

    private final JPAQueryFactory queryFactory;
    private final QDonation donation = QDonation.donation;
    private final QAccount account = QAccount.account;

    /**
     * 주어진 accountId에 해당하는 기부 내역을 조회하며,
     * 지정된 시작 날짜(startDate) 이후에 생성된 기부만 포함하고,
     * 생성일(createdAt)을 기준으로 내림차순 정렬하여 반환합니다.
     */
    @Override
    public Slice<Donation> findRecentDonationsByAccountId(
        final Long accountId,
        final LocalDateTime startDate,
        final Pageable pageable
    ) {
        List<Donation> content = queryFactory
            .selectFrom(donation)
            .where(
                donation.account.id.eq(accountId),
                donation.createdAt.goe(startDate)
            )
            .orderBy(donation.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return createSlice(content, pageable);
    }

    /**
     * 각 기부자의 총 기부 포인트와 마지막 기부 날짜를 기준으로 정렬 후 전체 기부자들의 정보를 반환합니다.
     */
    @Override
    public List<DonatorTotalDonationRepositoryDto> findAllDonatedDonatorsWithSort() {
        return queryFactory
            .select(Projections.constructor(
                DonatorTotalDonationRepositoryDto.class,
                account.id,
                account.nickname,
                account.profileImageUrl,
                donation.point.sum(),
                donation.createdAt.max()
            ))
            .from(donation)
            .join(donation.account, account)
            .groupBy(account.id, account.nickname, account.profileImageUrl)
            .orderBy(donation.point.sum().desc(), donation.createdAt.max().desc(), account.id.asc())
            .fetch();
    }
    private <T> Slice<T> createSlice(List<T> content, Pageable pageable) {
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
