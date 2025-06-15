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
        String imageUrl = node.has("coverLarge") && !node.get("coverLarge").asText().isBlank()
                ? node.get("coverLarge").asText()
                : (node.has("cover") ? node.get("cover").asText() : "");

        return new BookResponseDto(
                node.has("title") ? node.get("title").asText() : "",
                node.has("author") ? node.get("author").asText() : "",
                node.has("publisher") ? node.get("publisher").asText() : "",
                node.has("description") ? node.get("description").asText() : "",
                imageUrl,
                node.has("isbn13") ? node.get("isbn13").asText() : ""
        );
    }
}