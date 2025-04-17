package me.leeyeeun.bookhub.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.bookmark.controller.dto.response.BookmarkResponseDto;
import me.leeyeeun.bookhub.bookmark.entity.Bookmark;
import me.leeyeeun.bookhub.bookmark.repository.BookmarkRepository;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.community.repository.CommunityRepository;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public Community findCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티글이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return findMemberById(memberId);
    }

    @Transactional
    public Long saveBookmark(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = findCommunityById(communityId);

        boolean exists = bookmarkRepository.existsByMemberAndCommunity(member, community);
        if (exists) {
            throw new IllegalArgumentException("이미 북마크한 글입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .community(community)
                .build();

        return bookmarkRepository.save(bookmark).getId();
    }

    @Transactional
    public void deleteBookmark(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = findCommunityById(communityId);

        Bookmark bookmark = bookmarkRepository.findByMemberAndCommunity(member, community)
                .orElseThrow(() -> new IllegalArgumentException("북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getUserBookmarks(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);
        log.info("회원 id: {}", member.getId());

        return bookmarks.stream()
                .map(bookmark -> BookmarkResponseDto.from(bookmark.getCommunity()))
                .collect(Collectors.toList());
    }

}
