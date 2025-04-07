package me.leeyeeun.bookhub.member.controller.dto.response;

import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;

public record MemberInfoResponseDto(
        String pictureUrl,
        String name,
        String nickname,
        String introduction,
        String email,
        Role role
) {
    public MemberInfoResponseDto(Member member) {
        this(
                member.getPictureUrl(),
                member.getName(),
                member.getNickname(),
                member.getIntroduction(),
                member.getEmail(),
                member.getRole()
        );
    }
}
