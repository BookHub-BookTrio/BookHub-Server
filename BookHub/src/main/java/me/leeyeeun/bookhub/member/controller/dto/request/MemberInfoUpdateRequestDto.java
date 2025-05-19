package me.leeyeeun.bookhub.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;

public record MemberInfoUpdateRequestDto(
        String pictureUrl,
        @NotBlank(message = "필수 입력값입니다.")
        String name,
        @NotBlank(message = "필수 입력값입니다.")
        String nickname,
        String introduction,
        @NotBlank(message = "필수 입력값입니다.")
        String email
) {
    public MemberInfoUpdateRequestDto(Member member) {
        this(
                member.getPictureUrl(),
                member.getName(),
                member.getNickname(),
                member.getIntroduction(),
                member.getEmail()
        );
    }
}
