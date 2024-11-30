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
     * 기부자 순위를 조회하며, 각 기부자의 총 기부 포인트와 마지막 기부 날짜를 기준으로 정렬합니다.
     * 결과는 페이지네이션을 적용하여 반환합니다.
     */
    @Override
    public Slice<DonatorTotalDonationRepositoryDto> findDonatorRankings(final Pageable pageable) {
        List<DonatorTotalDonationRepositoryDto> content = queryFactory
            .select(createDonatorTotalDonationProjection())
            .from(donation)
            .join(donation.account, account)
            .groupBy(account.id, account.nickname, account.profileImageUrl)
            .orderBy(donation.point.sum().desc(), donation.createdAt.max().desc(), account.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return createSlice(content, pageable);
    }


    /**
     * 주어진 accountId에 해당하는 기부자의 총 기부 포인트와 마지막 기부 날짜를 조회하여 반환합니다.
     */
    @Override
    public Optional<DonatorTotalDonationRepositoryDto> findTotalDonationByAccountId(final Long accountId) {
        return Optional.ofNullable(queryFactory
            .select(createDonatorTotalDonationProjection())
            .from(donation)
            .where(account.id.eq(accountId))
            .join(donation.account, account)
            .groupBy(account.id, account.nickname, account.profileImageUrl)
            .fetchOne());
    }

    /**
     * 자신의 총 포인트보다 큰 계정 또는 총 포인트가 같고 자신보다 최근에 후원한 계정의 수 + 1을 반환합니다.
     */
    @Override
    public Long findRankByAccountId(final Long accountId) {
        QDonation comparisonDonation = new QDonation("comparisonDonation");

        JPQLQuery<Long> accountTotalPoints = getAccountTotalPoints(accountId);
        JPQLQuery<LocalDateTime> accountLastDonationDate = getAccountLastDonationDate(accountId);

        return queryFactory
            .select(comparisonDonation.account.id.countDistinct().add(1))
            .from(comparisonDonation)
            .where(createRankingCondition(comparisonDonation, accountTotalPoints, accountLastDonationDate))
            .fetchOne();
    }

    private BooleanExpression createRankingCondition(
        QDonation comparisonDonation,
        JPQLQuery<Long> accountTotalPoints,
        JPQLQuery<LocalDateTime> accountLastDonationDate
    ) {
        return accountTotalPoints.lt(getTotalDonationPointSubquery(comparisonDonation.account.id))
            .or(
                accountTotalPoints.eq(getTotalDonationPointSubquery(comparisonDonation.account.id))
                    .and(accountLastDonationDate.lt(getLastDonationDateSubquery(comparisonDonation.account.id)))
            );
    }

    private JPQLQuery<Long> getAccountTotalPoints(Long accountId) {
        return JPAExpressions
            .select(donation.point.sum())
            .from(donation)
            .where(donation.account.id.eq(accountId));
    }

    private JPQLQuery<LocalDateTime> getAccountLastDonationDate(Long accountId) {
        return JPAExpressions
            .select(donation.createdAt.max())
            .from(donation)
            .where(donation.account.id.eq(accountId));
    }

    private ConstructorExpression<DonatorTotalDonationRepositoryDto> createDonatorTotalDonationProjection() {
        return Projections.constructor(
            DonatorTotalDonationRepositoryDto.class,
            account.id,
            account.nickname,
            account.profileImageUrl,
            donation.point.sum(),
            donation.createdAt.max()
        );
    }

    private <T> Slice<T> createSlice(List<T> content, Pageable pageable) {
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }


    private JPAQuery<Long> getTotalDonationPointSubquery(NumberPath<Long> accountId) {
        return queryFactory
            .select(donation.point.sum())
            .from(donation)
            .where(donation.account.id.eq(accountId));
    }

    private JPAQuery<LocalDateTime> getLastDonationDateSubquery(NumberPath<Long> accountId) {
        return queryFactory
            .select(donation.createdAt.max())
            .from(donation)
            .where(donation.account.id.eq(accountId));
    }
}
