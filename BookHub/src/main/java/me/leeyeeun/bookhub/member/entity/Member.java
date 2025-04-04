package me.leeyeeun.bookhub.member.entity;

import jakarta.persistence.*;
import lombok.*;
import me.leeyeeun.bookhub.global.oauth2.kakao.domain.SocialType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "pictureUrl")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String refreshToken;

    @Builder
    public Member(String name, String email, String password, Long kakaoId, Role role, String pictureUrl,
                  SocialType socialType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
        this.role = role;
        this.pictureUrl = pictureUrl;
        this.socialType = socialType;
    }

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}

}