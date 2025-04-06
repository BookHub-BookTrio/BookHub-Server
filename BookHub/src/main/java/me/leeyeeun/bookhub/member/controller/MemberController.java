package me.leeyeeun.bookhub.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import me.leeyeeun.bookhub.member.controller.dto.request.MemberInfoUpdateRequestDto;
import me.leeyeeun.bookhub.member.controller.dto.response.MemberInfoResponseDto;
import me.leeyeeun.bookhub.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "회원 관련 API", description = "myPage의 회원 관련 API들 입니다.")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(method = "GET", description = "회원 정보 조회")
    public RspTemplate<MemberInfoResponseDto> getMemberInfo(Principal principal) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(principal);
        return RspTemplate.success(HttpStatus.OK,"회원 정보 조회 성공", memberInfoResponseDto);
    }

    @PutMapping
    @Operation(method = "PUT", description = "회원 정보 수정")
    public RspTemplate<MemberInfoUpdateRequestDto> updateMemberInfo(
            Principal principal,
            @RequestBody MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) {

        MemberInfoUpdateRequestDto updatedMember = memberService.updateMemberInfo(principal, memberInfoUpdateRequestDto);
        return RspTemplate.success(HttpStatus.OK, "회원 정보 수정 성공", updatedMember);
    }


}
