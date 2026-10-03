package com.example.errorfreetext.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpellerClient {

    private final RestClient restClient;

    @Value("${yandex.speller.url}")
    private String url;

    
    public List<SpellerError> check(String text, String language) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("text", text);
        form.add("lang", language);

        log.debug("calling yandex speller, text length {}", text.length());

        List<List<SpellerError>> response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null) {
            return List.of();
        }
        
        List<SpellerError> errors = response.stream()
                .flatMap(List::stream)
                .toList();
        log.debug("yandex returned {} errors", errors.size());
        return errors;
    }
}
