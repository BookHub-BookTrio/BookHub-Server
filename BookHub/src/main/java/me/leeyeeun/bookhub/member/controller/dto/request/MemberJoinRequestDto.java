package me.leeyeeun.bookhub.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberJoinRequestDto(
        @NotBlank(message = "필수 입력값입니다.")
        String name,
        @NotBlank(message = "필수 입력값입니다.")
        String nickname,
        @NotBlank(message = "필수 입력값입니다.")
        String email,
        @NotBlank(message = "필수 입력값입니다.")
        String password,
        @NotBlank(message = "필수 입력값입니다.")
        String passwordCheck
        ) {
}
