package me.leeyeeun.bookhub.global.oauth2.kakao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.exception.CustomException;
import me.leeyeeun.bookhub.global.exception.Error;
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
            throw new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage());
        }

        TokenDto tokenDto = tokenProvider.createToken(result.member());
        if (tokenDto == null) {
            throw new CustomException(Error.JWT_CREATION_EXCEPTION, Error.JWT_CREATION_EXCEPTION.getMessage());
        }

        return tokenDto;
    }

    @Transactional
    public TokenDto reIssueToken(RefreshToken refreshToken) {
        Member member = memberService.findByRefreshToken(refreshToken.refreshToken());
        return tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
    }
}
