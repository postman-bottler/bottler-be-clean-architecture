package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryResponseDTO(
        Long letterId,
        String title,
        String label,
        LetterType letterType,
        BoxType boxType,
        LocalDateTime createdAt
) {
}
