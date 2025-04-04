package me.leeyeeun.bookhub.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRequestDto(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name
) {
}
