package me.leeyeeun.bookhub.book.controller.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record BookResponseDto(
        String title,
        String author,
        String publisher,
        String description,
        String cover,
        String isbn13
) {
    public static BookResponseDto from(JsonNode node) {
        return new BookResponseDto(
                node.has("title") ? node.get("title").asText() : "",
                node.has("author") ? node.get("author").asText() : "",
                node.has("publisher") ? node.get("publisher").asText() : "",
                node.has("description") ? node.get("description").asText() : "",
                node.has("cover") ? node.get("cover").asText() : "",
                node.has("isbn13") ? node.get("isbn13").asText() : ""
        );
    }
}