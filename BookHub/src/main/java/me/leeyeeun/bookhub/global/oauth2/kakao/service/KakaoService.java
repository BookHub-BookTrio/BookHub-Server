package me.leeyeeun.bookhub.global.oauth2.kakao.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.global.exception.CustomException;
import me.leeyeeun.bookhub.global.exception.Error;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.LoginResult;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.SocialType;
import me.leeyeeun.bookhub.global.oauth2.kakao.dto.KakaoTokenResponseDto;
import me.leeyeeun.bookhub.global.oauth2.kakao.info.KakaoUserInfo;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.entity.Role;
import me.leeyeeun.bookhub.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                                    return Mono.error(new CustomException(Error.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION, error));
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
        KakaoUserInfo userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> {
                                    log.error("카카오 사용자 정보 요청 실패: {}", error);
                                    return Mono.error(new CustomException(Error.UNAUTHORIZED_ACCESS, error));
                                }))
                .bodyToMono(KakaoUserInfo.class)
                .block();

        if (userInfo== null) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "카카오 사용자 정보 수신 실패");
        }

        return userInfo;
    }

    @Transactional
    public LoginResult loginOrSignUp(String kakaoAccessToken) {
        KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);
        log.info("카카오 사용자 정보:", userInfo.toString());

        Long kakaoId = userInfo.getId();
        Optional<Member> member = getByKaKaoId(kakaoId);

        // 닉네임 기본값으로 카카오 닉네임 사용
        String nickname = userInfo.getKakaoAccount().getProfile().getNickName();

        // 닉네임이 비어있거나 중복이면 랜덤으로 대체
        if (nickname == null || nickname.isBlank() || memberRepository.existsByNickname(nickname)) {
            nickname = generateUniqueNickname();
        }

        if (member.isEmpty()) {
            member = Optional.of(memberRepository.save(
                    Member.builder()
                            .name(userInfo.getKakaoAccount().getProfile().getNickName())
                            .nickname(nickname)
                            .email(userInfo.getKakaoAccount().getEmail())
                            .kakaoId(kakaoId)
                            .socialType(SocialType.KAKAO)
                            .pictureUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                            .role(Role.ROLE_USER)
                            .build()));
        }

        return new LoginResult(member.get());

    }

    // 닉네임 자동 생성
    private static final String[] ADJECTIVES = {
            "귀여운", "상냥한", "멋진", "용감한", "지혜로운", "조용한", "활발한", "명랑한", "밝은", "차분한", "깜찍한", "사랑스러운"
    };

    private static final String[] NOUNS = {
            "개구리", "고양이", "강아지", "곰돌이", "햄스터", "수달", "토끼", "돼지", "원숭이", "양", "용", "판다", "사자", "호랑이", "캥거루", "쿼카"
    };

    private static final int MAX_ATTEMPTS = 100;
    private static final int RANDOM_SUFFIX_BOUND = 100;

    private String generateUniqueNickname() {
        int attempts = 0;
        String nickname;

        do {
            String adjective = ADJECTIVES[(int) (Math.random() * ADJECTIVES.length)];
            String noun = NOUNS[(int) (Math.random() * NOUNS.length)];
            int randomNumber = (int) (Math.random() * RANDOM_SUFFIX_BOUND);
            nickname = adjective + noun + randomNumber;

            attempts++;
            if (attempts > MAX_ATTEMPTS) {
                throw new RuntimeException("랜덤 닉네임 생성을 실패했습니다. 중복이 너무 많으니 다시 시도해주세요.");
            }
        } while (memberRepository.existsByNickname(nickname));

        return nickname;
    }

    @Transactional(readOnly = true)
    public Optional<Member> getByKaKaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }
}
