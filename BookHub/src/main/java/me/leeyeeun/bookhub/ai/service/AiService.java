package me.leeyeeun.bookhub.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=%s";

    public String getBookSummary(String bookTitle) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> message = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", bookTitle +
                                        "라는 책의 줄거리를 간결하고 가독성 좋게 한글로 요약해줘." +
                                        "'~입니다' 형식으로 친절하게 말해주고 4줄 이상 넘어가지 않도록 해줘. " +
                                        "이모티콘도 사용해주는데 너무 많이 사용하진 마. " +
                                        "마지막엔 어떤 사람에게 이 책을 추천하는지도 알려줘. " +
                                        "거짓된 내용은 절대 아는 척하면서 작성하지 마.")
                        ))
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

        String url = String.format(GEMINI_API_URL, apiKey);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);

            JsonNode candidates = response.getBody().path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("content").path("parts").get(0).path("text").asText();
            } else {
                return "요약을 가져오지 못했습니다 ..🥲 다시 시도 부탁드려요!";
            }
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔 에러 출력
            return "요약 중 오류 발생: " + e.getMessage();
        }
    }
}
