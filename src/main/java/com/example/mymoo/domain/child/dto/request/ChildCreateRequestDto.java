package com.example.mymoo.domain.child.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChildCreateRequestDto(
    @NotNull(message = "accountId는 필수 항목입니다.")
    Long accountId,

    @NotBlank(message = "cardNumber는 필수 항목입니다")
    @Pattern(regexp = "^\\d{16}$", message = "카드 번호는 16자리 숫자여야 합니다.")
    String cardNumber,

    @NotBlank(message = "Do는 필수 항목입니다")
    String Do,

    @NotBlank(message = "Sigun은 필수 항목입니다")
    String sigun,

    @NotBlank(message = "gu는 필수 항목입니다")
    String gu
) {

}