package me.leeyeeun.bookhub.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.community.controller.dto.request.CommunityRequestDto;
import me.leeyeeun.bookhub.community.controller.dto.response.CommunityResponseDto;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.community.service.CommunityService;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
@Tag(name = "커뮤니티 API", description = "CommunityPage 관련 API들 입니다.")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    @Operation(method = "POST", summary = "커뮤니티글 생성", description = "커뮤니티글을 생성합니다.")
    public RspTemplate<?> createWish(Principal principal, @Valid @RequestBody CommunityRequestDto commnuityRequestDto) {
        Long communityId = communityService.createCommunity(commnuityRequestDto, principal);
        return RspTemplate.success(HttpStatus.CREATED, "위시글 작성 성공", communityId);
    }

    @PutMapping
    @Operation(method = "PUT", summary = "커뮤니티글 수정", description = "커뮤니티글을 수정합니다.")
    public RspTemplate<?> updateCommunity(
            Principal principal,
            @Valid @RequestParam Long id,
            @RequestBody CommunityRequestDto commnuityRequestDto
    ) {
        communityService.updateCommunity(id, commnuityRequestDto, principal);
        return RspTemplate.success(HttpStatus.OK, "커뮤니티글 수정 성공", id);
    }

    @DeleteMapping
    @Operation(method = "DELETE", summary = "커뮤니티글 삭제", description = "커뮤니티글을 삭제합니다.")
    public RspTemplate<?> deleteCommunity(Principal principal, @RequestParam Long id) {
        communityService.deleteCommunity(id, principal);
        return RspTemplate.success(HttpStatus.OK, "커뮤니티글 삭제 성공", id);
    }

    @GetMapping("/detail")
    @Operation(summary = "커뮤니티글 한 개 조회", description = "커뮤니티글 한 개를 조회합니다.")
    public RspTemplate<CommunityResponseDto> getCommunity(@RequestParam Long id, Principal principal) {
        Community community = communityService.getCommunity(id);
        CommunityResponseDto commnuityResponseDto = CommunityResponseDto.from(community);
        return RspTemplate.success(HttpStatus.OK, "커뮤니티글 조회 성공", commnuityResponseDto);
    }

    @GetMapping
    @Operation(method = "GET", summary = "커뮤니티글 전체 조회", description = "전체 커뮤니티글을 조회합니다.")
    public RspTemplate<List<CommunityResponseDto>> getAllCommunities() {
        List<Community> communities = communityService.getAllCommunities();
        List<CommunityResponseDto> commnuityResponseDtos = communities.stream()
                .map(CommunityResponseDto::from)
                .toList();
        return RspTemplate.success(HttpStatus.OK, "전체 커뮤니티글 조회 성공", commnuityResponseDtos);
    }

    @GetMapping("/my")
    @Operation(method = "GET", summary = "본인이 작성한 커뮤니티글 조회", description = "본인이 작성한 커뮤니티글만 조회합니다.")
    public RspTemplate<List<CommunityResponseDto>> getMyCommunities(Principal principal) {
        List<Community> communities = communityService.getMyCommunities(principal);
        List<CommunityResponseDto> commnuityResponseDtos = communities.stream()
                .map(CommunityResponseDto::from)
                .toList();
        return RspTemplate.success(HttpStatus.OK, "본인이 작성한 커뮤니티글 조회 성공", commnuityResponseDtos);
    }
}
