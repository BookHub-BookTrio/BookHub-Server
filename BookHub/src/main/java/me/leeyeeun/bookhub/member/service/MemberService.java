package me.leeyeeun.bookhub.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.member.controller.dto.response.MemberLoginRequestDto;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException( "해당 멤버를 찾지 못했습니다.")
        );
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버를 찾지 못했습니다.")
        );
    }

    // Todo: 회원가입 작성하기

    // 자체 로그인
    @Transactional
    public Member login(MemberLoginRequestDto memberRequestDto) {
        Member member = findByEmail(memberRequestDto.email());
        return member;
    }

    // Todo: 회원탈퇴 로직 작성하기 (자체로그인)
}
