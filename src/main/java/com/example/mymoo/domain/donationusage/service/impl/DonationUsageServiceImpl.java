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
import com.example.mymoo.domain.donationusage.dto.request.DonationUsageUpdateMessageRequestDto;
import com.example.mymoo.domain.donationusage.entity.DonationUsage;
import com.example.mymoo.domain.donationusage.exception.DonationUsageException;
import com.example.mymoo.domain.donationusage.exception.DonationUsageExceptionDetails;
import com.example.mymoo.domain.donationusage.repository.DonationUsageRepository;
import com.example.mymoo.domain.donationusage.service.DonationUsageService;
import com.example.mymoo.domain.email.EmailClient;
import com.example.mymoo.domain.email.dto.EmailSendDTO;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.repository.StoreRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.example.mymoo.global.aop.log.LogExecutionTime;
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
    private final EmailClient emailClient;

    @Override
    public DonationUsage useDonation(
        final Long storeAccountId,
        final DonationUsageCreateRequestDto donationUsageCreateRequestDto
    ) {
        Donation donation = donationRepository.findById(donationUsageCreateRequestDto.donationId())
            .orElseThrow(() -> new DonationException(DonationExceptionDetails.DONATION_NOT_FOUND));
        Child child = childRepository.findByAccount_Id(donationUsageCreateRequestDto.childAccountId())
            .orElseThrow(() -> new ChildException(ChildExceptionDetails.CHILD_NOT_FOUND));

        // 자신의 가게가 아닌 다른 가게의 후원을 이용하려 할 때
        Store storeUsingDonation = donation.getStore();
        List<Store> storesOwns = storeRepository.findAllByAccount_Id(storeAccountId);
        Long storeUsingDonationId = storeUsingDonation.getId();
        boolean hasMatchingStore = storesOwns.stream()
            .anyMatch(store -> store.getId().equals(storeUsingDonationId));
        if (!hasMatchingStore){
            throw new DonationUsageException(DonationUsageExceptionDetails.FORBIDDEN_ACCESS_TO_OTHER_STORE);
        }

        // 후원 사용을 요청한 CHILD 계정이 당일에 후원권을 사용한 이력이 2회 이상 있는 경우
        LocalDate today = LocalDate.now();
        long usageCountToday = donationUsageRepository.countByChildAndCreatedAtBetween(
            child,
            today.atStartOfDay(),
            today.atTime(LocalTime.MAX)
        );
        if (usageCountToday >= 2) {
            throw new DonationUsageException(DonationUsageExceptionDetails.EXCEEDED_DAILY_USAGE_LIMIT);
        }

        // 사용 여부 업데이트
        donation.setIsUsedToTrue();
        // 사용 가능한 후원 금액 감소
        storeUsingDonation.useUsableDonation(donation.getPoint());
        // store 계정의 point 증가. 향후 현금으로 바꿀 수 있음
        storeUsingDonation.getAccount().chargePoint(donation.getPoint());

        DonationUsage donationUsage =  donationUsageRepository.save(
            DonationUsage.builder()
                .child(child)
                .donation(donation)
                .build()
        );
        emailClient.sendDonationUsageMail(EmailSendDTO.from(donationUsage));
        return donationUsage;
    }

    @Override
    public void updateMessage(
        final Long childAccountId,
        final DonationUsageUpdateMessageRequestDto donationUsageUpdateMessageRequestDto
    ) {
        DonationUsage donationUsage = donationUsageRepository.findByDonation_id(donationUsageUpdateMessageRequestDto.donationId())
            .orElseThrow(() -> new DonationUsageException(DonationUsageExceptionDetails.DONATION_USAGE_NOT_FOUND));

        // 자신이 사용하지 않은 후원에 대해 감사 메시지 작성하려 할 때
        if (!Objects.equals(donationUsage.getChild().getAccount().getId(), childAccountId)){
            throw new DonationUsageException(DonationUsageExceptionDetails.FORBIDDEN_TO_WRITE_MESSAGE_TO_OTHER_DONATION);
        }

        // 메시지 업데이트
        donationUsage.setMessage(donationUsageUpdateMessageRequestDto.message());
    }
}
