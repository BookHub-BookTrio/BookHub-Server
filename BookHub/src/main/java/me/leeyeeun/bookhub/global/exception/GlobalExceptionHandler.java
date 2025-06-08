package me.leeyeeun.bookhub.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        Error error = e.getError();
        ErrorResponse errorResponse = new ErrorResponse(error.name(), error.getMessage());
        return ResponseEntity.status(error.getHttpStatus()).body(errorResponse);
    }

    // 그 외 RuntimeException 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 에러가 발생했습니다.");
        return ResponseEntity.status(500).body(errorResponse);
    }
}