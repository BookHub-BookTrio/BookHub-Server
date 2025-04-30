package me.leeyeeun.bookhub.wish.controller.dto.response;

import me.leeyeeun.bookhub.wish.entity.Category;

public record CategoryPercentageResponseDto(
        Category category,
        double percentage
) {
}
