package me.leeyeeun.bookhub.community.service;

import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.bookmark.repository.BookmarkRepository;
import me.leeyeeun.bookhub.community.controller.dto.request.CommunityRequestDto;
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

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public Community findCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY, Error.NOT_FOUND_COMMUNITY.getMessage()));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
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
    public Long createCommunity(CommunityRequestDto commnuityRequestDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        Community community = Community.builder()
                .title(commnuityRequestDto.title())
                .content(commnuityRequestDto.content())
                .member(member)
                .build();

        return communityRepository.save(community).getId();
    }

    @Transactional
    public Community updateCommunity(Long communityId, CommunityRequestDto commnuityRequestDto, Principal principal) {
        Community community = findCommunityById(communityId);
        Member member = getMemberFromPrincipal(principal);

        if (!community.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        community.update(commnuityRequestDto);
        return community;
    }

    @Transactional
    public void deleteCommunity(Long communityId, Principal principal) {
        Community community = findCommunityById(communityId);
        Member member = getMemberFromPrincipal(principal);

        if (!community.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        bookmarkRepository.deleteAllByCommunity(community);

        communityRepository.delete(community);
    }

    @Transactional(readOnly = true)
    public Community getCommunity(Long id) {
        return findCommunityById(id);
    }

    @Transactional(readOnly = true)
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Community> getMyCommunities(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        return communityRepository.findAllByMemberId(member.getId());
    }
}