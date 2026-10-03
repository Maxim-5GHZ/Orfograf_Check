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

    // отдаляем текст яндексу, options считаем на месте по тексту
    public List<SpellerError> check(String text, String language) {
        int options = SpellerOptionsResolver.resolve(text);
        return check(text, language, options);
    }

    // основной метод: нарезаем длинный текст, правим каждый кусок и склеиваем обратно
    public String correct(String text, String language) {
        // options один на весь текст, как и написано в тз
        int options = SpellerOptionsResolver.resolve(text);
        List<String> chunks = TextChunker.chunk(text);
        log.debug("correcting text length {} in {} chunks", text.length(), chunks.size());

        StringBuilder result = new StringBuilder();
        for (String chunk : chunks) {
            List<SpellerError> errors = check(chunk, language, options);
            result.append(CorrectionApplier.apply(chunk, errors));
        }
        return result.toString();
    }

    public List<SpellerError> check(String text, String language, int options) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("text", text);
        form.add("lang", language);
        form.add("options", String.valueOf(options));

        log.debug("calling yandex speller, text length {}, options {}", text.length(), options);

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
