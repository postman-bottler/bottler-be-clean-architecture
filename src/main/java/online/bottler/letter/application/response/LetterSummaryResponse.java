package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.adapter.out.persistence.model.LetterSummaryProjection;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryResponse(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                                    LocalDateTime createdAt) {
    public static LetterSummaryResponse from(LetterSummaryProjection projection) {
        return new LetterSummaryResponse(projection.letterId(), projection.title(), projection.label(),
                projection.letterType(), projection.boxType(), projection.createdAt());
    }

    public static List<LetterSummaryResponse> fromList(List<LetterSummaryProjection> projections) {
        return projections.stream().map(LetterSummaryResponse::from).toList();
    }
}
