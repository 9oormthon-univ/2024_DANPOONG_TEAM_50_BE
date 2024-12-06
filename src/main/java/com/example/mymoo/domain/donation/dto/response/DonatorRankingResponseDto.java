package com.example.mymoo.domain.donation.dto.response;

import com.example.mymoo.domain.account.exception.AccountException;
import com.example.mymoo.domain.account.exception.AccountExceptionDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Slice;

@Builder
public record DonatorRankingResponseDto(
    @NonNull DonatorRankingDto donatorRanking,

    @NonNull List<DonatorTotalDonationRepositoryDto> rankings
) {
    public static DonatorRankingResponseDto from(
        List<DonatorTotalDonationRepositoryDto> sortedDonators,
        Long accountId
    ){
        return DonatorRankingResponseDto.builder()
            .donatorRanking(DonatorRankingDto.from(sortedDonators, accountId))
            .rankings(sortedDonators.subList(0, Math.min(5, sortedDonators.size())))
            .build();
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
            List<DonatorTotalDonationRepositoryDto> sortedDonators,
            Long accountId
        ) {
            return findDonatorByAccountId(sortedDonators, accountId)
                .map(searchResultDto -> DonatorRankingDto.builder()
                    .donator(searchResultDto.donator().donator())
                    .profileImageUrl(searchResultDto.donator().profileImageUrl())
                    .totalDonation(searchResultDto.donator().totalDonation())
                    .lastDonatedAt(searchResultDto.donator().lastDonatedAt())
                    .rank(searchResultDto.rank())
                    .build())
                .orElseThrow(() -> new AccountException(AccountExceptionDetails.ACCOUNT_NOT_FOUND));
        }

        private static Optional<DonatorSearchResult> findDonatorByAccountId(
            List<DonatorTotalDonationRepositoryDto> sortedDonators,
            Long accountId
        ) {
            for (int i = 0; i < sortedDonators.size(); i++) {
                DonatorTotalDonationRepositoryDto donatorDto = sortedDonators.get(i);
                if (donatorDto.accountId().equals(accountId)) {
                    return Optional.of(new DonatorSearchResult(donatorDto, (long) (i + 1)));
                }
            }
            return Optional.empty();
        }

        private record DonatorSearchResult(
            DonatorTotalDonationRepositoryDto donator,
            Long rank
        ) {}
    }
}
