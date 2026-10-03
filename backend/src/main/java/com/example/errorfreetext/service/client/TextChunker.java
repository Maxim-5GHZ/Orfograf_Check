package com.example.errorfreetext.service.client;

import java.util.ArrayList;
import java.util.List;

// яндекс принимает максимум 10000 символов за раз, длинный текст режем
public final class TextChunker {

    private static final int LIMIT = 10000;

    private TextChunker() {
    }

    public static List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (text.length() - start > LIMIT) {
            int end = start + LIMIT;

            // режем по последнему пробелу или переносу, чтобы не разрезать слово пополам
            int space = text.lastIndexOf(' ', end - 1);
            int newline = text.lastIndexOf('\n', end - 1);
            int cut = Math.max(space, newline);
            if (cut <= start) {
                // слово длиннее лимита, тут уже без вариантов
                cut = end;
            } else {
                cut = cut + 1;
            }

            chunks.add(text.substring(start, cut));
            start = cut;
        }
        chunks.add(text.substring(start));
        return chunks;
    }
}
