package me.leeyeeun.bookhub.member.controller.dto.response;

public record MemberLoginRequestDto(
        String email,
        String password
)
{
}
