package me.leeyeeun.bookhub.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import me.leeyeeun.bookhub.member.controller.dto.response.MemberInfoResponseDto;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "회원 관련 API", description = "myPage의 회원 관련 API들 입니다.")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(method = "GET", description = "회원 조회")
    public RspTemplate<MemberInfoResponseDto> getMemberInfo(Principal principal) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(principal);
        return RspTemplate.success(HttpStatus.OK,"회원 조회 성공", memberInfoResponseDto);
    }

}
