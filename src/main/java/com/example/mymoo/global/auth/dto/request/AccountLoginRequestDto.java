package com.example.mymoo.global.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountLoginRequestDto(
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
    String password
) {

}
