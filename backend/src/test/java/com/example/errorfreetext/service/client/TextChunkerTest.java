package com.example.errorfreetext.service.client;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextChunkerTest {

    @Test
    void shortTextStaysOneChunk() {
        List<String> chunks = TextChunker.chunk("просто текст");
        assertEquals(1, chunks.size());
        assertEquals("просто текст", chunks.get(0));
    }

    @Test
    void longTextSplitUnderLimitAndJoinsBack() {
        StringBuilder text = new StringBuilder();
        while (text.length() < 25000) {
            text.append("слово которое должно быть целым ");
        }
        String input = text.toString();

        List<String> chunks = TextChunker.chunk(input);

        assertTrue(chunks.size() > 1);
        assertTrue(chunks.stream().allMatch(c -> c.length() <= 10000));
        assertEquals(input, String.join("", chunks));
    }

    @Test
    void doesNotCutWordInMiddle() {
        StringBuilder text = new StringBuilder();
        while (text.length() < 25000) {
            text.append("слово которое должно быть целым ");
        }
        List<String> chunks = TextChunker.chunk(text.toString());

        // кроме самого последнего куска все должны кончаться пробелом или переносом
        for (int i = 0; i < chunks.size() - 1; i++) {
            String chunk = chunks.get(i);
            char last = chunk.charAt(chunk.length() - 1);
            assertTrue(last == ' ' || last == '\n',
                    "чанк " + i + " обрывается посреди слова: ..." + chunk.substring(chunk.length() - 20));
        }
    }

    @Test
    void wordLongerThanLimitCutHard() {
        StringBuilder word = new StringBuilder();
        while (word.length() < 12000) {
            word.append("а");
        }
        String input = word.toString();

        List<String> chunks = TextChunker.chunk(input);

        assertTrue(chunks.size() > 1);
        assertEquals(input, String.join("", chunks));
    }

    @Test
    void textWithNewlinesSplitsByNewline() {
        StringBuilder text = new StringBuilder();
        while (text.length() < 12000) {
            text.append("строка текста и перенос строки\n");
        }
        String input = text.toString();

        List<String> chunks = TextChunker.chunk(input);

        assertEquals(input, String.join("", chunks));
        assertFalse(chunks.get(0).isEmpty());
    }
}
