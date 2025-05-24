package online.bottler.letter.v2.application.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryResponse(
        Long letterId,
        String title,
        String label,
        LetterType letterType,
        BoxType boxType,
        LocalDateTime createdAt
) {
}
