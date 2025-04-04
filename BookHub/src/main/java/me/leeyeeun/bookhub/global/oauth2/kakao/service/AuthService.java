package me.leeyeeun.bookhub.global.oauth2.kakao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.LoginResult;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.RefreshToken;
import me.leeyeeun.bookhub.global.oauth2.kakao.dto.TokenDto;
import me.leeyeeun.bookhub.global.token.TokenProvider;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @Transactional
    public TokenDto signUpOrSignIn(String socialAccessToken) {
        LoginResult result = null;

        String accessToken = kakaoService.getAccessToken(socialAccessToken);
        result = kakaoService.loginOrSignUp(accessToken);

        if (result == null) {
            throw new IllegalArgumentException("회원 정보가 없습니다.");
        }

        TokenDto tokenDto = tokenProvider.createToken(result.member());

        return tokenDto;
    }

    @Transactional
    public TokenDto reIssueToken(RefreshToken refreshToken) {
        Member member = memberService.findByRefreshToken(refreshToken.refreshToken());
        return tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
    }

    // 로그아웃
    @Transactional
    public void logOut(Long userId) {
        Member member = memberService.findById(userId);
        member.updateRefreshToken(null);
    }

    // Todo: 회원탈퇴 로직 작성하기 (소셜로그인)
}
