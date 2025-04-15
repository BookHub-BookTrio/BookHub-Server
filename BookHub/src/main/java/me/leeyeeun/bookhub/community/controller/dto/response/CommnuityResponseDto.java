package me.leeyeeun.bookhub.community.controller.dto.response;

import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.member.entity.Member;

import java.time.LocalDateTime;

public record CommnuityResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdat,
        String nickname
) {
    public static CommnuityResponseDto from(Community community) {
        Member member = community.getMember();
        String nickname = member != null ? member.getNickname() : "별명 없음";

        return new CommnuityResponseDto(
                community.getId(),
                community.getTitle(),
                community.getContent(),
                community.getCreatedat(),
                nickname
        );
    }
}
