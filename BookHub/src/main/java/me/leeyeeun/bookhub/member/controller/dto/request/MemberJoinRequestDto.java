package me.leeyeeun.bookhub.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberJoinRequestDto(
        @NotBlank

        String name,
        @NotBlank

        String nickname,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String passwordCheck
        ) {
}
