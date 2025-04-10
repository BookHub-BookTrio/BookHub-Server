package me.leeyeeun.bookhub.wish.controller.dto.request;

import me.leeyeeun.bookhub.wish.entity.Category;
import me.leeyeeun.bookhub.wish.entity.Progress;
import me.leeyeeun.bookhub.wish.entity.Star;

public record WishRequestDto(
        String bookname,

        String author,

        String content,

        Progress progress,

        Category category,

        Star star
) {
}
