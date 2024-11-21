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
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.domain.store.repository.StoreRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DonationUsageServiceImpl implements DonationUsageService {

    private final DonationRepository donationRepository;
    private final ChildRepository childRepository;
    private final DonationUsageRepository donationUsageRepository;

    @Override
    public void useDonation(
        final Long storeAccountId,
        final DonationUsageCreateRequestDto donationUsageCreateRequestDto
    ) {
        Donation donation = donationRepository.findById(donationUsageCreateRequestDto.donationId())
                .orElseThrow(() -> new DonationException(DonationExceptionDetails.DONATION_NOT_FOUND));
        Child child = childRepository.findByAccount_Id(donationUsageCreateRequestDto.childAccountId())
                .orElseThrow(() -> new ChildException(ChildExceptionDetails.CHILD_NOT_FOUND));
        Store store = donation.getStore();
        // 자신의 가게가 아닌 다른 가게의 후원을 사용하려 할 때
        if (!Objects.equals(store.getAccount().getId(), storeAccountId)){
            throw new DonationUsageException(DonationUsageExceptionDetails.FORBIDDEN_ACCESS_TO_OTHER_STORE);
        }

        // 사용 여부 업데이트
        donation.setIsUsedToTrue();
        // 사용 가능한 후원 금액 감소
        store.useUsableDonation(donation.getPoint());
        // store 계정의 point 증가. 향후 현금으로 바꿀 수 있음
        store.getAccount().chargePoint(donation.getPoint());

        donationUsageRepository.save(
            DonationUsage.builder()
                .child(child)
                .donation(donation)
                .build()
        );
    }
}
