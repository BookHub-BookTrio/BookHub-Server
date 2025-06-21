package me.leeyeeun.bookhub.wish.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.leeyeeun.bookhub.wish.entity.Category;
import me.leeyeeun.bookhub.wish.entity.Progress;
import me.leeyeeun.bookhub.wish.entity.Star;

public record WishRequestDto(
        @NotBlank(message = "필수 입력값입니다.")
        String bookname,

        @NotBlank(message = "필수 입력값입니다.")
        String author,

        @NotBlank(message = "필수 입력값입니다.")
        String content,

        @NotNull
        Progress progress,

        @NotNull
        Category category,

        @NotNull
        Star star
) {
}
