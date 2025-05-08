package me.leeyeeun.bookhub.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.leeyeeun.bookhub.book.controller.dto.BookResponseDto;
import me.leeyeeun.bookhub.book.service.BookService;
import me.leeyeeun.bookhub.global.template.RspTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
@Tag(name = "알라딘 도서 추천 API", description = "알라딘 OpenAPI를 활용한 추천 도서 API들 입니다.")
public class BookController {

    private final BookService bookService;

    @GetMapping("/random")
    @Operation(method = "GET", summary = "랜덤 추천 도서 조회", description = "랜덤 추천 도서 2권을 조회합니다.")
    public RspTemplate<List<BookResponseDto>> getRandomBooks() {
        return RspTemplate.success(HttpStatus.OK, "랜덤 추천 도서", bookService.getRandomBooks());
    }

    @GetMapping("/bestseller")
    @Operation(method = "GET", summary = "베스트샐러 도서 조회", description = "베스트샐러 도서 3권을 조회합니다.")
    public RspTemplate<List<BookResponseDto>> getBestsellers() {
        return RspTemplate.success(HttpStatus.OK, "베스트셀러 도서", bookService.getBestsellers());
    }

    @GetMapping("/new")
    @Operation(method = "GET", summary = "신간 도서 조회", description = "신간 도서 3권을 조회합니다.")
    public RspTemplate<List<BookResponseDto>> getNewBooks() {
        return RspTemplate.success(HttpStatus.OK, "신간 도서", bookService.getNewBooks());
    }
}