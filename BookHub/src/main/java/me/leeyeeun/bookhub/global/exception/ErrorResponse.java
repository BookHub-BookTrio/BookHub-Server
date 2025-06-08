package me.leeyeeun.bookhub.global.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {}