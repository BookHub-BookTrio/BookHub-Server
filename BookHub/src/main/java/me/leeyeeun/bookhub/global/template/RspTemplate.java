package me.leeyeeun.bookhub.global.template;

import lombok.*;
import org.springframework.http.HttpStatus;

// 응답 템플릿
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RspTemplate<T> {
    private final int statusCode;
    private final String message;
    T data;

    public RspTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public RspTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }

    public static <T> RspTemplate<T> success(HttpStatus status, String message, T data) {
        return new RspTemplate<>(status.value(), message, data);
    }

}