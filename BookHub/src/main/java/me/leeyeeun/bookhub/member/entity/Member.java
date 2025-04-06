package me.leeyeeun.bookhub.member.entity;

import jakarta.persistence.*;
import lombok.*;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.SocialType;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-z A-Z0-9가-힣]*$");
    private static final int MAX_NICKNAME_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "pictureUrl")
    private String pictureUrl;

    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String refreshToken;

    @Builder
    public Member(String name, String nickname, String email, String password, Long kakaoId, Role role, String pictureUrl,
                  SocialType socialType) {
        validateNickname(nickname);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
        this.role = role;
        this.pictureUrl = pictureUrl;
        this.socialType = socialType;
    }

    private void validateNickname(String nickname) {
        Matcher matcher = NICKNAME_PATTERN.matcher(nickname);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        if (nickname.isEmpty() || nickname.length() >= MAX_NICKNAME_LENGTH) {
            throw new IllegalArgumentException(String.format("닉네임은 1자 이상 %d자 이하여야 합니다.", MAX_NICKNAME_LENGTH));
        }
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void update(String name,
                       String nickname,
                       String introduction,
                       String email) {
        this.name = updateName(name);
        this.nickname = updateNickname(nickname);
        this.introduction = updateIntrodution(introduction);
        this.email = updateEmail(email);
    }

    public String updateName(String name) {
        if (name == null || name.isBlank())
            return this.name;
        return name;
    }

    public String updateNickname(String nickname) {
        if (nickname == null || nickname.isBlank())
            return this.nickname;
        return nickname;
    }

    public String updateIntrodution(String introduction) {
        return introduction;
    }

    public String updateEmail(String email) {
        if (email == null || email.isBlank())
            return this.email;
        return email;
    }
}