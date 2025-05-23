package me.leeyeeun.bookhub.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.exception.CustomException;
import me.leeyeeun.bookhub.global.exception.Error;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import me.leeyeeun.bookhub.wish.controller.dto.request.WishRequestDto;
import me.leeyeeun.bookhub.wish.controller.dto.response.CategoryPercentageResponseDto;
import me.leeyeeun.bookhub.wish.entity.Category;
import me.leeyeeun.bookhub.wish.entity.Wish;
import me.leeyeeun.bookhub.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Wish findWishById(Long id) {
        return wishRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.WISH_NOT_FOUND, Error.WISH_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return findMemberById(memberId);
    }

    @Transactional
    public Long createWish(WishRequestDto wishRequestDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        Wish wish = Wish.builder()
                .bookname(wishRequestDto.bookname())
                .author(wishRequestDto.author())
                .content(wishRequestDto.content())
                .progress(wishRequestDto.progress())
                .category(wishRequestDto.category())
                .star(wishRequestDto.star())
                .member(member)
                .build();

        return wishRepository.save(wish).getId();
    }

    @Transactional
    public Wish updateWish(Long wishId, WishRequestDto wishRequestDto, Principal principal) {
        Wish wish = findWishById(wishId);
        Member member = getMemberFromPrincipal(principal);

        if (!wish.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        wish.update(wishRequestDto);
        return wish;
    }

    @Transactional
    public void deleteWish(Long wishId, Principal principal) {
        Wish wish = findWishById(wishId);
        Member member = getMemberFromPrincipal(principal);

        if (!wish.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        wishRepository.delete(wish);
    }

    @Transactional(readOnly = true)
    public List<Wish> getAllWishes(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        return wishRepository.findAllByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    public Wish findMyWishById(Long wishId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        return wishRepository.findByIdAndMemberId(wishId, member.getId())
                .orElseThrow(() -> new CustomException(Error.WISH_NOT_FOUND, "해당 독서록이 존재하지 않거나 회원에게 권한이 없습니다. id=" + wishId));
    }

    @Transactional(readOnly = true)
    public List<Wish> searchWishesByBookname(String keyword) {
        return wishRepository.findByBooknameContainingIgnoreCase(keyword);
    }

    // 월별 읽은 책 개수 통계
    @Transactional(readOnly = true)
    public long countWishesByYearAndMonth(Principal principal, int year, int month) {
        Member member = getMemberFromPrincipal(principal);

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        return wishRepository.findAllByMemberIdAndCreatedAtBetween(
                member.getId(),
                startOfMonth,
                endOfMonth
        ).size();
    }

    // 장르 통계
    @Transactional(readOnly = true)
    public List<CategoryPercentageResponseDto> getCategoryGenre(Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        List<Wish> allWishes = wishRepository.findAllByMemberId(member.getId());

        int total = allWishes.size();
        if (total == 0) return List.of(); // 위시글 없으면 빈 리스트

        Map<Category, Long> countByCategory = allWishes.stream()
                .filter(w -> w.getCategory() != null)
                .collect(Collectors.groupingBy(Wish::getCategory, Collectors.counting()));

        // 퍼센트로 변환
        return countByCategory.entrySet().stream()
                .map(entry -> new CategoryPercentageResponseDto(
                        entry.getKey(),
                        Math.round((entry.getValue() * 100.0 / total) * 10) / 10.0 // 소수점 첫째 자리까지 나타냄 (반올림)
                ))
                .sorted(Comparator.comparing(CategoryPercentageResponseDto::percentage).reversed()) // 내림차순
                .toList();
    }
}
