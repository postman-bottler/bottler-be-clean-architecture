package online.bottler.letter.domain;

import java.time.LocalDateTime;

public record LetterSummary(
        Long letterId,
        String title, String label,
        LetterType letterType, BoxType boxType,
        LocalDateTime createdAt
) {

    public static LetterSummary of(
            Long letterId,
            String title, String label,
            LetterType letterType, BoxType boxType,
            LocalDateTime createdAt
    ) {
        return new LetterSummary(
                letterId,
                title, label,
                letterType, boxType,
                createdAt
        );
    }
}
