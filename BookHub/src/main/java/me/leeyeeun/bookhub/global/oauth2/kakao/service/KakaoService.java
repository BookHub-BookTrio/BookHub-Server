package me.leeyeeun.bookhub.global.oauth2.kakao.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.LoginResult;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.SocialType;
import me.leeyeeun.bookhub.global.oauth2.kakao.dto.KakaoTokenResponseDto;
import me.leeyeeun.bookhub.global.oauth2.kakao.info.KakaoUserInfo;
import me.leeyeeun.bookhub.global.token.Token;
import me.leeyeeun.bookhub.global.token.TokenProvider;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com"; // 액세스 토큰 발급 서버
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com"; // 사용자 정보 서버

    private final MemberRepository memberRepository;

    // 인가 코드를 통해 액세스 토큰을 받아오는 메서드
    public String getAccessToken(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> {
                                    log.error("카카오 OAuth 요청 정보: clientId={}, clientSecret={}", clientId, clientSecret);
                                    log.error("카카오 OAuth 토큰 요청 실패: {}", error);
                                    return Mono.error(new RuntimeException("카카오 OAuth 토큰 요청 실패"));
                                })
                )
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        if (kakaoTokenResponseDto == null || kakaoTokenResponseDto.getAccessToken() == null) {
            throw new IllegalArgumentException("카카오 액세스 토큰 발급 실패");
        }

        return kakaoTokenResponseDto.getAccessToken();

    }

    // 액세스 토큰을 이용해 카카오로부터 사용자 정보를 받아오는 메서드
    public KakaoUserInfo getUserInfo(String accessToken) {
        return WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    @Transactional
    public LoginResult loginOrSignUp(String kakaoAccessToken) {
        KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);
        log.info("카카오 사용자 정보:", userInfo.toString());

        Long kakaoId = userInfo.getId();
        Optional<Member> member = getByKaKaoId(kakaoId);

        if (member.isEmpty()) {
            member = Optional.of(memberRepository.save(
                    Member.builder()
                            .name(userInfo.getKakaoAccount().getProfile().getNickName())
                            .email(userInfo.getKakaoAccount().getEmail())
                            .kakaoId(kakaoId)
                            .socialType(SocialType.KAKAO)
                            .pictureUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                            .role(Role.ROLE_USER)
                            .build()));
        }

        return new LoginResult(member.get());

    }

    public void deleteMember(final Member member) {
        HashMap responseMap = null;

        try {
            responseMap = WebClient.create(KAUTH_USER_URL_HOST)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path("/v1/user/unlink")
                            .queryParam("target_id_type", "user_id")
                            .queryParam("target_id", member.getId())
                            .build(true))
                    .header(HttpHeaders.CONTENT_TYPE,
                            HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + clientSecret)
                    .retrieve()
                    .bodyToMono(HashMap.class)
                    .block();
        }catch (Exception e){
            log.info(e.getMessage());
        }
        log.info("삭제된 카카오 사용자 id:"+ responseMap.get("id").toString());
    }

    @Transactional(readOnly = true)
    public Optional<Member> getByKaKaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }
}
