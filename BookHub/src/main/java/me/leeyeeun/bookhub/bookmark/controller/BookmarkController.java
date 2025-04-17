package me.leeyeeun.bookhub.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.bookmark.controller.dto.response.BookmarkResponseDto;
import me.leeyeeun.bookhub.bookmark.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/bookmark")
@Tag(name = "위시글(독서록) 북마크 API", description = "WishPage 북마크 관련 API들 입니다.")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @Operation(method = "POST", summary = "북마크 추가", description = "북마크를 추가합니다.")
    public ResponseEntity<String> addBookmark(@RequestParam Long communityId,
                                              Principal principal) {
        bookmarkService.saveBookmark(communityId, principal);
        return ResponseEntity.ok("북마크 추가 완료");
    }

    @DeleteMapping
    @Operation(method = "DELETE", summary = "북마크 삭제", description = "북마크를 삭제합니다.")
    public ResponseEntity<String> removeBookmark(@RequestParam Long communityId,
                                                 Principal principal) {
        bookmarkService.deleteBookmark(communityId, principal);
        return ResponseEntity.ok("북마크 삭제 완료");
    }

    @GetMapping
    @Operation(method = "GET", summary = "내가 북마크한 북마크 조회", description = "북마크를 조회합니다.")
    public ResponseEntity<List<BookmarkResponseDto>> getBookmarks(Principal principal) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(principal));
    }

}