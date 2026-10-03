package com.example.errorfreetext.service.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// подставляет исправления из яндекса в текст
public final class CorrectionApplier {

    private CorrectionApplier() {
    }

    public static String apply(String text, List<SpellerError> errors) {
        if (errors == null || errors.isEmpty()) {
            return text;
        }
        // копируем и сортируем по убыванию позиции,
        // правим с конца строки иначе позиции следующих слов поедут
        List<SpellerError> sorted = new ArrayList<>(errors);
        sorted.sort(Comparator.comparingInt(SpellerError::getPos).reversed());

        StringBuilder result = new StringBuilder(text);
        for (SpellerError error : sorted) {
            if (error.getS() == null || error.getS().isEmpty()) {
                // яндекс не смог предложить вариант, оляем слово как есть
                continue;
            }
            int pos = error.getPos();
            int len = error.getLen();
            if (pos < 0 || len < 0 || pos + len > result.length()) {
                continue;
            }
            result.replace(pos, pos + len, error.getS().get(0));
        }
        return result.toString();
    }
}
