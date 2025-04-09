package me.leeyeeun.bookhub.wish.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import me.leeyeeun.bookhub.wish.controller.dto.request.WishRequestDto;
import me.leeyeeun.bookhub.wish.controller.dto.response.WishResponseDto;
import me.leeyeeun.bookhub.wish.entity.Wish;
import me.leeyeeun.bookhub.wish.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish")
@Tag(name = "희망도서 API", description = "WishPage 관련 API들입니다.")
public class WishController {

    private final WishService wishService;

    @PostMapping
    @Operation(method = "POST", description = "위시글 작성")
    public RspTemplate<?> createWish(Principal principal, @RequestBody WishRequestDto wishRequestDto) {
        Long wishId = wishService.createWish(wishRequestDto, principal);
        return RspTemplate.success(HttpStatus.CREATED, "희망도서 글 작성 성공", wishId);
    }

    @PutMapping
    @Operation(method = "PUT", description = "위시글 수정")
    public RspTemplate<?> updateWish(
            Principal principal,
            @RequestParam Long id,
            @RequestBody WishRequestDto wishRequestDto
    ) {
        wishService.updateWish(id, wishRequestDto, principal);
        return RspTemplate.success(HttpStatus.OK, "희망도서 수정 성공", id);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "위시글 삭제")
    public RspTemplate<?> deleteWish(Principal principal, @RequestParam Long id) {
        wishService.deleteWish(id, principal);
        return RspTemplate.success(HttpStatus.OK, "희망도서 삭제 성공", id);
    }

    @GetMapping("/detail")
    @Operation(summary = "위시글 한 개 조회", description = "id를 기반으로 위시글을 조회합니다.")
    public RspTemplate<Wish> getWish(@RequestParam Long id) {
        return RspTemplate.success(HttpStatus.OK, "희망도서 조회 성공", wishService.findWishById(id));
    }


    @GetMapping
    @Operation(method = "GET", description = "전체 위시글 조회")
    public RspTemplate<List<WishResponseDto>> getAllWishes() {
        List<Wish> wishes = wishService.getAllWishes();
        List<WishResponseDto> wishResponseDto = wishes.stream()
                .map(WishResponseDto::from)
                .toList();
        return RspTemplate.success(HttpStatus.OK, "전체 희망도서 조회 성공", wishResponseDto);
    }
}
