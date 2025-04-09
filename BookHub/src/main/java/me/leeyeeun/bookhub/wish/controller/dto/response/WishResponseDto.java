package me.leeyeeun.bookhub.wish.controller.dto.response;

import me.leeyeeun.bookhub.wish.entity.Category;
import me.leeyeeun.bookhub.wish.entity.Progress;
import me.leeyeeun.bookhub.wish.entity.Star;
import me.leeyeeun.bookhub.wish.entity.Wish;

public record WishResponseDto(
        Long id,

        String bookname,

        String author,

        String content,

        Progress progress,

        Category category,

        Star star
) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(
                wish.getId(),
                wish.getBookname(),
                wish.getAuthor(),
                wish.getContent(),
                wish.getProgress(),
                wish.getCategory(),
                wish.getStar()
        );
    }
}
