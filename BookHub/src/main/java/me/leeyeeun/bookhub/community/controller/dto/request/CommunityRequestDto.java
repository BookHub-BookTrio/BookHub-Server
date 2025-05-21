package me.leeyeeun.bookhub.community.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommunityRequestDto(
        @NotBlank(message = "필수 입력값입니다.")
        String title,
        @NotBlank(message = "필수 입력값입니다.")
        String content
) {
}
