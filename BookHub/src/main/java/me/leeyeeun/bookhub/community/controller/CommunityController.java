package me.leeyeeun.bookhub.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.community.controller.dto.request.CommnuityRequestDto;
import me.leeyeeun.bookhub.community.controller.dto.response.CommnuityResponseDto;
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
    public RspTemplate<?> createWish(Principal principal, @RequestBody CommnuityRequestDto commnuityRequestDto) {
        Long communityId = communityService.createCommunity(commnuityRequestDto, principal);
        return RspTemplate.success(HttpStatus.CREATED, "위시글 작성 성공", communityId);
    }

    @PutMapping
    @Operation(method = "PUT", summary = "커뮤니티글 수정", description = "커뮤니티글을 수정합니다.")
    public RspTemplate<?> updateCommunity(
            Principal principal,
            @RequestParam Long id,
            @RequestBody CommnuityRequestDto commnuityRequestDto
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
    public RspTemplate<Community> getCommunity(@RequestParam Long id) {
        return RspTemplate.success(HttpStatus.OK, "커뮤니티글 조회 성공", communityService.findCommunityById(id));
    }

    @GetMapping
    @Operation(method = "GET", summary = "커뮤니티글 전체 조회", description = "전체 커뮤니티글을 조회합니다.")
    public RspTemplate<List<CommnuityResponseDto>> getAllCommunities() {
        List<Community> communities = communityService.getAllCommunities();
        List<CommnuityResponseDto> commnuityResponseDtos = communities.stream()
                .map(CommnuityResponseDto::from)
                .toList();
        return RspTemplate.success(HttpStatus.OK, "전체 커뮤니티글 조회 성공", commnuityResponseDtos);
    }
}
