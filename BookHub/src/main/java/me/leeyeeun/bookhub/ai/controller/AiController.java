package me.leeyeeun.bookhub.ai.controller;

import me.leeyeeun.bookhub.ai.controller.dto.AiRequestDto;
import me.leeyeeun.bookhub.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<String> getBookSummary(@RequestBody AiRequestDto aiRequestDto) {
        String result = aiService.getBookSummary(aiRequestDto.bookTitle());
        return ResponseEntity.ok(result);
    }
}
