package me.leeyeeun.bookhub.global.oauth2.kakao.domain;

import me.leeyeeun.bookhub.member.entity.Member;

public record LoginResult(Member member) {
    public static LoginResult from(final Member member){
        return new LoginResult(member);
    }
}