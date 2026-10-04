package com.example.errorfreetext.service.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpellerOptionsResolverTest {

    @Test
    void plainTextOnlyBaseOptions() {
        int options = SpellerOptionsResolver.resolve("обычный текст без ничего");
        assertEquals(0, options);
    }

    @Test
    void digitsAddIgnoreDigits() {
        int options = SpellerOptionsResolver.resolve("пароль 123");
        assertEquals(2, options);
    }

    @Test
    void urlAddIgnoreUrls() {
        int options = SpellerOptionsResolver.resolve("смотри https://ya.ru");
        assertEquals(4, options);
    }

    @Test
    void digitsAndUrlTogether() {
        int options = SpellerOptionsResolver.resolve("http://example.com 42");
        assertEquals(6, options);
    }

    @Test
    void digitsOnNextLineStillCounted() {
        int options = SpellerOptionsResolver.resolve("текст\nещё 7 цифр");
        assertEquals(2, options);
    }

    @Test
    void wwwUrlDetected() {
        int options = SpellerOptionsResolver.resolve("заходи на www.example.com");
        assertEquals(4, options);
    }
}
