package me.leeyeeun.bookhub.book.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class AladinClient {

    @Value("${aladin.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public JsonNode fetchBooks(String queryType, int maxResults) {
        String url = String.format(
                "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=%s&QueryType=%s&MaxResults=%d&start=1&SearchTarget=Book&output=js&Version=20131101",
                apiKey, queryType, maxResults
        );

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        log.info("알라딘 응답 ({}): {}", queryType, response);

        return restTemplate.getForObject(url, JsonNode.class);
    }
}
