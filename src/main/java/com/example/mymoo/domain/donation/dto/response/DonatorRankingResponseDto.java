package com.example.mymoo.domain.donation.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Slice;

@Builder
public record DonatorRankingResponseDto(
    @NonNull DonatorRankingDto donatorRanking,

    @NonNull DonatorRankingListDto rankings
) {
    public static DonatorRankingResponseDto from(
        DonatorTotalDonationRepositoryDto donatorTotalDonationRepositoryDto,
        Long donatorRank,
        Slice<DonatorTotalDonationRepositoryDto> donatorTotalDonationRepositoryDtoSlice
    ){
        return DonatorRankingResponseDto.builder()
            .donatorRanking(DonatorRankingDto.from(donatorTotalDonationRepositoryDto, donatorRank))
            .rankings(DonatorRankingListDto.from(donatorTotalDonationRepositoryDtoSlice))
            .build();
    }

    @Builder
    public record DonatorRankingListDto(
        @NonNull List<DonatorTotalDonationRepositoryDto> donations,
        boolean hasNext,
        int numberOfElements,
        int pageNumber,
        int pageSize
    ) {
        public static DonatorRankingListDto from(
            Slice<DonatorTotalDonationRepositoryDto> donatorTotalDonationRepositoryDtoSlice) {
            return DonatorRankingListDto.builder()
                .donations(donatorTotalDonationRepositoryDtoSlice.toList())
                .hasNext(donatorTotalDonationRepositoryDtoSlice.hasNext())
                .numberOfElements(donatorTotalDonationRepositoryDtoSlice.getNumberOfElements())
                .pageNumber(donatorTotalDonationRepositoryDtoSlice.getNumber())
                .pageSize(donatorTotalDonationRepositoryDtoSlice.getSize())
                .build();
        }
    }

    @Builder
    public record DonatorRankingDto(
        @NotBlank String donator,
        @NotBlank String profileImageUrl,
        @NotNull Long totalDonation,
        @NotNull LocalDateTime lastDonatedAt,
        @NotNull Long rank
    ) {
        public static DonatorRankingDto from(
            DonatorTotalDonationRepositoryDto donatorTotalDonationRepositoryDto,
            Long donatorRank
        ){
            return DonatorRankingDto.builder()
                .donator(donatorTotalDonationRepositoryDto.donator())
                .profileImageUrl(donatorTotalDonationRepositoryDto.profileImageUrl())
                .totalDonation(donatorTotalDonationRepositoryDto.totalDonation())
                .lastDonatedAt(donatorTotalDonationRepositoryDto.lastDonatedAt())
                .rank(donatorRank)
                .build();
        }
    }
}
