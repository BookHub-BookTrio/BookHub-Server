package me.leeyeeun.bookhub.bookmark.controller.dto.response;

import me.leeyeeun.bookhub.community.entity.Community;

import java.time.LocalDateTime;

public record BookmarkResponseDto(
        Long id,
        String title,
        String nickname,
        LocalDateTime createdat
) {
    public static BookmarkResponseDto from(Community community) {
        String nickname = (community.getMember() != null) ? community.getMember().getNickname() : "별명 없음";

        return new BookmarkResponseDto(
                community.getId(),
                community.getTitle(),
                nickname,
                community.getCreatedat()
        );
    }
}
