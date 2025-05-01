package me.leeyeeun.bookhub.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.leeyeeun.bookhub.book.client.AladinClient;
import me.leeyeeun.bookhub.book.controller.dto.BookResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final AladinClient aladinClient;

    public List<BookResponseDto> getBestsellers() {
        return convert(aladinClient.fetchBooks("Bestseller", 12));
    }

    public List<BookResponseDto> getNewBooks() {
        return convert(aladinClient.fetchBooks("ItemNewSpecial", 12));
    }

    public List<BookResponseDto> getRandomBooks() {
        List<BookResponseDto> books = convert(aladinClient.fetchBooks("BlogBest", 12));
        if (books.isEmpty()) {
            log.warn("에디터 추천 도서 없음. 베스트셀러로 대체.");
            books = getBestsellers();
        }
        Collections.shuffle(books);
        return books.stream().limit(2).toList();
    }


    private List<BookResponseDto> convert(JsonNode response) {
        List<BookResponseDto> books = new ArrayList<>();
        if (response == null || !response.has("item")) {
            log.warn("Aladin API 응답이 비어있거나 item 필드가 없음: {}", response);
            return books;
        }
        for (JsonNode item : response.get("item")) {
            books.add(BookResponseDto.from(item));
        }

        return books;
    }
}