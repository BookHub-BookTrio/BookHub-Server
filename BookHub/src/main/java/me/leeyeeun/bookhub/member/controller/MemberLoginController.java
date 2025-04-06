package me.leeyeeun.bookhub.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import me.leeyeeun.bookhub.global.token.TokenProvider;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberJoinRequestDto;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberLoginRequestDto;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal")
@Tag(name = "회원가입/자체 로그인 API", description = "회원가입 자체 로그인과 관련된 API들입니다. access-refresh 토큰 형태로 진행되며, 이쪽 API를 제외하고는 다른 API들 사용시 Autorization을 통해 Bearer 형식으로 토큰을 입력받아 사용합니다.")
public class MemberLoginController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    @Operation(method = "POST", description = "자체 로그인을 진행해 로그인을 진행합니다.")
    public RspTemplate<?> signInInternal(
            @RequestBody @Valid MemberLoginRequestDto memberRequestDto
    ) {
        Member member = memberService.login(memberRequestDto);
        return RspTemplate.success(HttpStatus.OK , "로그인 성공", tokenProvider.createToken(member));
    }

    @PostMapping("/join")
    @Operation(method = "POST", description = "자체 로그인을 진행해 회원가입을 진행합니다.")
    public RspTemplate<?> joinInternal(
            @RequestBody @Valid MemberJoinRequestDto memberJoinRequestDto
    ) {
        Member member = memberService.join(memberJoinRequestDto);
        return RspTemplate.success(HttpStatus.OK,"회원가입 성공", tokenProvider.createToken(member));
    }
}
