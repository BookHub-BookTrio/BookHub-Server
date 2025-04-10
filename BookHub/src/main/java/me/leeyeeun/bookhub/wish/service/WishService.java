package me.leeyeeun.bookhub.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import me.leeyeeun.bookhub.wish.controller.dto.request.WishRequestDto;
import me.leeyeeun.bookhub.wish.entity.Wish;
import me.leeyeeun.bookhub.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Wish findWishById(Long id) {
        return wishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("위시글이 존재하지 않습니다."));
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
    public Long createWish(WishRequestDto requestDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        Wish wish = Wish.builder()
                .bookname(requestDto.bookname())
                .author(requestDto.author())
                .content(requestDto.content())
                .progress(requestDto.progress())
                .category(requestDto.category())
                .star(requestDto.star())
                .member(member)
                .build();

        return wishRepository.save(wish).getId();
    }

    @Transactional
    public Wish updateWish(Long wishId, WishRequestDto requestDto, Principal principal) {
        Wish wish = findWishById(wishId);
        getMemberFromPrincipal(principal);

        wish.update(requestDto);
        return wish;
    }

    @Transactional
    public void deleteWish(Long wishId, Principal principal) {
        Wish wish = findWishById(wishId);
        getMemberFromPrincipal(principal);

        wishRepository.delete(wish);
    }

    @Transactional(readOnly = true)
    public List<Wish> getAllWishes() {
        return wishRepository.findAll();
    }
}
