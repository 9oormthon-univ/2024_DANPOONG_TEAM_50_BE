package com.example.mymoo.domain.donationusage.service.impl;

import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.exception.ChildException;
import com.example.mymoo.domain.child.exception.ChildExceptionDetails;
import com.example.mymoo.domain.child.repository.ChildRepository;
import com.example.mymoo.domain.donation.entity.Donation;
import com.example.mymoo.domain.donation.exception.DonationException;
import com.example.mymoo.domain.donation.exception.DonationExceptionDetails;
import com.example.mymoo.domain.donation.repository.DonationRepository;
import com.example.mymoo.domain.donationusage.dto.request.DonationUsageCreateRequestDto;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import com.example.mymoo.domain.donationusage.exception.DonationUsageException;
import com.example.mymoo.domain.donationusage.exception.DonationUsageExceptionDetails;
import com.example.mymoo.domain.donationusage.repository.DonationUsageRepository;
import com.example.mymoo.domain.donationusage.service.DonationUsageService;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.repository.StoreRepository;
import java.util.List;
import java.util.Objects;

import com.example.mymoo.global.aop.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional @LogExecutionTime
@RequiredArgsConstructor
public class DonationUsageServiceImpl implements DonationUsageService {

    private final DonationRepository donationRepository;
    private final ChildRepository childRepository;
    private final DonationUsageRepository donationUsageRepository;
    private final StoreRepository storeRepository;

    @Override
    public void useDonation(
        final Long storeAccountId,
        final DonationUsageCreateRequestDto donationUsageCreateRequestDto
    ) {
        Donation donation = donationRepository.findById(donationUsageCreateRequestDto.donationId())
            .orElseThrow(() -> new DonationException(DonationExceptionDetails.DONATION_NOT_FOUND));
        Child child = childRepository.findByAccount_Id(donationUsageCreateRequestDto.childAccountId())
            .orElseThrow(() -> new ChildException(ChildExceptionDetails.CHILD_NOT_FOUND));

        Store storeUsingDonation = donation.getStore();
        // 해당 가게 계정이 가진 store 들의 id들 뽑기
        List<Store> storesOwns = storeRepository.findAllByAccount_Id(storeAccountId);
        Long storeUsingDonationId = storeUsingDonation.getId();
        boolean hasMatchingStore = storesOwns.stream()
            .anyMatch(store -> store.getId().equals(storeUsingDonationId));
        if (!hasMatchingStore){
            throw new DonationUsageException(DonationUsageExceptionDetails.FORBIDDEN_ACCESS_TO_OTHER_STORE);
        }

        // 사용 여부 업데이트
        donation.setIsUsedToTrue();
        // 사용 가능한 후원 금액 감소
        storeUsingDonation.useUsableDonation(donation.getPoint());
        // store 계정의 point 증가. 향후 현금으로 바꿀 수 있음
        storeUsingDonation.getAccount().chargePoint(donation.getPoint());

        donationUsageRepository.save(
            DonationUsage.builder()
                .child(child)
                .donation(donation)
                .build()
        );
    }
}
