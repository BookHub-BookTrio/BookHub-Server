package me.leeyeeun.bookhub.member.controller.dto.request;

import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;

public record MemberInfoUpdateRequestDto(
        String name,
        String nickname,
        String introduction,
        String email
) {
    public MemberInfoUpdateRequestDto(Member member) {
        this(
                member.getName(),
                member.getNickname(),
                member.getIntroduction(),
                member.getEmail()
        );
    }
}
