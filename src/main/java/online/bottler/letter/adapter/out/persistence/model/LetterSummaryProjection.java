package online.bottler.letter.adapter.out.persistence.model;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryProjection(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                                      LocalDateTime createdAt) {
    public LetterSummary toDomain() {
        return LetterSummary.of(letterId, title, label, letterType, boxType, createdAt);
    }

    public static List<LetterSummary> toDomainList(List<LetterSummaryProjection> letterSummaryProjections) {
        return letterSummaryProjections.stream().map(LetterSummaryProjection::toDomain).toList();
    }
}
