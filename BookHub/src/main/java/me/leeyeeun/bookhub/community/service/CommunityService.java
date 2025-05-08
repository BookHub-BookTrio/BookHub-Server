package me.leeyeeun.bookhub.community.service;

import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.community.controller.dto.request.CommnuityRequestDto;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.community.repository.CommunityRepository;
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

    @Transactional(readOnly = true)
    public Community findCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티글이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return findMemberById(memberId);
    }

    @Transactional
    public Long createCommunity(CommnuityRequestDto commnuityRequestDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        Community community = Community.builder()
                .title(commnuityRequestDto.title())
                .content(commnuityRequestDto.content())
                .member(member)
                .build();

        return communityRepository.save(community).getId();
    }

    @Transactional
    public Community updateCommunity(Long communityId, CommnuityRequestDto commnuityRequestDto, Principal principal) {
        Community community = findCommunityById(communityId);
        getMemberFromPrincipal(principal);

        community.update(commnuityRequestDto);
        return community;
    }

    @Transactional
    public void deleteCommunity(Long communityId, Principal principal) {
        Community community = findCommunityById(communityId);
        getMemberFromPrincipal(principal);

        communityRepository.delete(community);
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