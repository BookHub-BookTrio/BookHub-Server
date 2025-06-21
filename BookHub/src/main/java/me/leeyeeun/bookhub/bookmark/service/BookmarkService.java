package me.leeyeeun.bookhub.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.bookmark.controller.dto.response.BookmarkResponseDto;
import me.leeyeeun.bookhub.bookmark.entity.Bookmark;
import me.leeyeeun.bookhub.bookmark.repository.BookmarkRepository;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.community.repository.CommunityRepository;
import me.leeyeeun.bookhub.global.exception.CustomException;
import me.leeyeeun.bookhub.global.exception.Error;
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
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
    }

    @Transactional(readOnly = true)
    public Community findCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY, Error.NOT_FOUND_COMMUNITY.getMessage()));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new CustomException(Error.UN_AUTHORIZED, Error.UN_AUTHORIZED.getMessage());
        }
        try {
            Long memberId = Long.parseLong(principal.getName());
            return findMemberById(memberId);
        } catch (NumberFormatException e) {
            throw new CustomException(Error.UN_AUTHORIZED, Error.UN_AUTHORIZED.getMessage());
        }
    }

    @Transactional
    public Long saveBookmark(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = findCommunityById(communityId);

        boolean exists = bookmarkRepository.existsByMemberAndCommunity(member, community);
        if (exists) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
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
                .orElseThrow(() -> new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getUserBookmarks(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);
        log.info("회원 id: {}", member.getId());

        return bookmarks.stream()
                .map(BookmarkResponseDto::from)
                .collect(Collectors.toList());
    }

}
