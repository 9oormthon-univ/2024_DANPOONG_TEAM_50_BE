package com.example.mymoo.domain.donation.dto.response;

import com.example.mymoo.domain.donation.entity.Donation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Slice;

@Builder
public record ReadStoreDonationListResponseDto(
    @NonNull List<DonationDto> donations,
    boolean hasNext,
    int numberOfElements,
    int pageNumber,
    int pageSize
) {

    public static ReadStoreDonationListResponseDto from(Slice<Donation> donationSlice) {
        List<DonationDto> donationDtos = donationSlice.getContent()
            .stream()
            .map(DonationDto::from)
            .toList();

        return ReadStoreDonationListResponseDto.builder()
            .donations(donationDtos)
            .hasNext(donationSlice.hasNext())
            .numberOfElements(donationSlice.getNumberOfElements())
            .pageNumber(donationSlice.getNumber())
            .pageSize(donationSlice.getSize())
            .build();
    }
    @Builder
    public record DonationDto(
        Long donationId,
        Long point,
        String donator,
        LocalDateTime donatedAt
    ) {
        public static DonationDto from(Donation donation) {
            return DonationDto.builder()
                .donationId(donation.getId())
                .point(donation.getPoint())
                .donator(donation.getAccount().getNickname())
                .donatedAt(donation.getCreatedAt())
                .build();
        }
    }
}