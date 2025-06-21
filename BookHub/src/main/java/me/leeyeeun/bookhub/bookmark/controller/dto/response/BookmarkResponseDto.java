package me.leeyeeun.bookhub.bookmark.controller.dto.response;

import me.leeyeeun.bookhub.bookmark.entity.Bookmark;
import me.leeyeeun.bookhub.community.entity.Community;

import java.time.LocalDateTime;

public record BookmarkResponseDto(
        Long id,
        String title,
        String nickname,
        LocalDateTime communityCreatedAt, // 커뮤니티글 작성일
        LocalDateTime bookmarkedAt // 북마크 생성일
) {
    public static BookmarkResponseDto from(Bookmark bookmark) {
        Community community = bookmark.getCommunity();
        String nickname = (community.getMember() != null) ? community.getMember().getNickname() : "별명 없음";

        return new BookmarkResponseDto(
                community.getId(),
                community.getTitle(),
                nickname,
                community.getCreatedAt(), // 커뮤니티글 작성일
                bookmark.getCreatedAt() // 북마크 생성일
        );
    }
}
