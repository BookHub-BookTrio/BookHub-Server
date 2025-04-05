package me.leeyeeun.bookhub.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequestDto(
        @NotBlank
        String email,
        @NotBlank
        String password
)
{
}
