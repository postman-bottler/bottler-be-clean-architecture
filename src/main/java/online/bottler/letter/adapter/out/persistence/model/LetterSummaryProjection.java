package online.bottler.letter.adapter.out.persistence.model;

import java.time.LocalDateTime;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryProjection(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                                      LocalDateTime createdAt) {
}
