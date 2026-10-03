package com.example.errorfreetext.service.client;

import java.util.regex.Pattern;

// считаем options для яндекса по документации:
// https://yandex.ru/dev/speller/doc/dg/reference/checkTexts.html
public final class SpellerOptionsResolver {

    //.IGNORE_DIGITS - пропускать слова с цифрами
    public static final int IGNORE_DIGITS = 2;

    // IGNORE_URLS - пропускать интернет-адреса
    public static final int IGNORE_URLS = 4;

    // FIND_REPEAT_WORDS - искать повторяющиеся слова
    public static final int FIND_REPEAT_WORDS = 8;

    // IGNORE_CAPITALIZATION - не ругаться на капс
    public static final int IGNORE_CAPITALIZATION = 512;

    // цифра где угодно, (?s) чтобы перенос строки не ломал проверку
    private static final Pattern DIGITS = Pattern.compile("(?s).*\\d.*");

    // http://, https://, www. или просто домен вроде example.com
    private static final Pattern URL = Pattern.compile(
            "(https?://\\S+)|(www\\.\\S+)|([a-z0-9-]+\\.[a-z]{2,}(\\S*)?)",
            Pattern.CASE_INSENSITIVE);

    private SpellerOptionsResolver() {
    }

    public static int resolve(String text) {
        int options = FIND_REPEAT_WORDS | IGNORE_CAPITALIZATION;

        if (DIGITS.matcher(text).matches()) {
            options |= IGNORE_DIGITS;
        }
        if (URL.matcher(text).find()) {
            options |= IGNORE_URLS;
        }
        return options;
    }
}
