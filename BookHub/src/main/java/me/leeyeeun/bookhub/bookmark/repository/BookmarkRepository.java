package me.leeyeeun.bookhub.bookmark.repository;

import me.leeyeeun.bookhub.bookmark.entity.Bookmark;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberAndCommunity(Member member, Community community);
    Optional<Bookmark> findByMemberAndCommunity(Member member, Community community);
    List<Bookmark> findAllByMember(Member member);
}
