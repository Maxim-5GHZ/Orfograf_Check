package com.example.errorfreetext.service.client;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorrectionApplierTest {

    @Test
    void appliesAllErrors() {
        String text = "это ашипка и ешо ошибка";
        List<SpellerError> errors = List.of(
                new SpellerError(4, 6, "ашипка", List.of("ошибка")),
                new SpellerError(13, 3, "ешо", List.of("ещё"))
        );
        assertEquals("это ошибка и ещё ошибка", CorrectionApplier.apply(text, errors));
    }

    @Test
    void emptyTextWithoutErrorsUnchanged() {
        assertEquals("привет мир", CorrectionApplier.apply("привет мир", List.of()));
    }

    @Test
    void errorWithoutSuggestionsSkipped() {
        String text = "это ашипка";
        List<SpellerError> errors = List.of(
                new SpellerError(4, 6, "ашипка", List.of())
        );
        assertEquals(text, CorrectionApplier.apply(text, errors));
    }

    @Test
    void brokenPositionSkipped() {
        String text = "это ашипка";
        List<SpellerError> errors = List.of(
                new SpellerError(999, 5, "x", List.of("y"))
        );
        assertEquals(text, CorrectionApplier.apply(text, errors));
    }

    @Test
    void positionsStayValidWhenApplyingFromEnd() {
        // если бы применяли слева направо, второй отрезок уже съехал бы
        String text = "аа бб ббб";
        List<SpellerError> errors = List.of(
                new SpellerError(0, 2, "аа", List.of("х")),
                new SpellerError(3, 2, "бб", List.of("уу"))
        );
        assertEquals("х уу ббб", CorrectionApplier.apply(text, errors));
    }
}
