package com.example.mymoo.domain.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountCreateRequestDto(
    @NotBlank(message = "이메일은 필수 항목입니다")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "이메일 형식을 지켜주세요"
    )
    @Schema(description = "사용자의 이메일 주소(이메일 형식에 맞아야 함)", example = "donator@example.com")
    String email,

    @NotBlank(message = "비밀번호는 필수 항목입니다")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
        message = "비밀번호는 8자 이상이어야 하며, 숫자, 영문자, 특수문자를 포함해야 합니다"
    )
    @Schema(description = "사용자의 비밀번호 (숫자, 영문자, 특수문자 1개 이상씩 포함 총 8자 이상)", example = "Password123!")
    String password,

    @NotBlank(message = "닉네임은 필수 항목입니다")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s]+$", message = "닉네임은 영어, 숫자, 한글 및 띄어쓰기를 포함할 수 있습니다.")
    @Schema(description = "사용자의 닉네임(2~10). 영어, 숫자, 한글 및 띄어쓰기를 포함할 수 있습니다. ", example = "마이무 화이팅a1")
    String nickname,

    @NotBlank(message = "전화번호는 필수 항목입니다")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리의 숫자여야 합니다")
    @Schema(description = "사용자의 전화번호 (10~11자리 숫자)", example = "01012345678")
    String phoneNumber,

    @NotBlank(message = "사용자 역할은 필수 항목입니다")
    @Pattern(
        regexp = "^(DONATOR|CHILD|STORE|ADMIN)$",
        message = "DONATOR, CHILD, STORE, ADMIN 중 하나를 입력해주세요"
    )
    @Schema(description = "사용자의 역할 (DONATOR, CHILD, STORE, ADMIN 중 하나)", example = "DONATOR")
    String userRole,

    @NotBlank(message = "카드번호는 필수 항목입니다")
    @Pattern(regexp = "^\\d{16}$", message = "카드 번호는 16자리 숫자여야 합니다.")
            String cardNumber
) {

}
