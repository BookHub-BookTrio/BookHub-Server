package me.leeyeeun.bookhub.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.exception.CustomException;
import me.leeyeeun.bookhub.global.exception.Error;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberInfoUpdateRequestDto;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberJoinRequestDto;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberLoginRequestDto;
import me.leeyeeun.bookhub.member.controller.dto.response.MemberInfoResponseDto;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
    }

    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new CustomException(Error.REFRESH_TOKEN_EXPIRED_EXCEPTION, Error.REFRESH_TOKEN_EXPIRED_EXCEPTION.getMessage())
        );
    }

    // 회원가입
    @Transactional
    public Member join(MemberJoinRequestDto memberJoinRequestDto) {
        // 존재하는 이메일 검증
        if (memberRepository.existsByEmail(memberJoinRequestDto.email())) {
            throw new CustomException(Error.EXIST_USER, Error.EXIST_USER.getMessage());
        }

        // 비밀번호와 비밀번호 확인이 같은지 검증
        if (!memberJoinRequestDto.password().equals(memberJoinRequestDto.passwordCheck())) {
            throw new CustomException(Error.BAD_REQUEST_PASSWORD, Error.BAD_REQUEST_PASSWORD.getMessage());
        }

        Member member = Member.builder()
                .name(memberJoinRequestDto.name())
                .nickname(memberJoinRequestDto.nickname())
                .email(memberJoinRequestDto.email())
                .password(passwordEncoder.encode(memberJoinRequestDto.password()))
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);
        return member;
    }

    // 자체 로그인
    @Transactional
    public Member login(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = findByEmail(memberLoginRequestDto.email());
        if (!passwordEncoder.matches(memberLoginRequestDto.password(), member.getPassword())) {
            throw new CustomException(Error.BAD_REQUEST_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(Principal principal) {
        String id = principal.getName();
        Member member = findById(Long.valueOf(id));

        return new MemberInfoResponseDto(
                member.getPictureUrl(),
                member.getName(),
                member.getNickname(),
                member.getIntroduction(),
                member.getEmail(),
                member.getRole()
        );
    }

    @Transactional
    public MemberInfoUpdateRequestDto updateMemberInfo(Principal principal, final MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) {
        String id = principal.getName();
        Member member = findById(Long.valueOf(id));

        member.update(
                memberInfoUpdateRequestDto.pictureUrl(),
                memberInfoUpdateRequestDto.name(),
                memberInfoUpdateRequestDto.nickname(),
                memberInfoUpdateRequestDto.introduction(),
                memberInfoUpdateRequestDto.email());

        return new MemberInfoUpdateRequestDto(member);
    }

}
