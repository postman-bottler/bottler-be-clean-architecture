package online.bottler.letter.application.response;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.LetterType;

public record LetterSummaryResponse(Long letterId, String title, String label, LetterType letterType, BoxType boxType,
                                    LocalDateTime createdAt) {
    public static LetterSummaryResponse from(LetterSummary letterSummary) {
        return new LetterSummaryResponse(letterSummary.getLetterId(), letterSummary.getTitle(),
                letterSummary.getLabel(), letterSummary.getLetterType(), letterSummary.getBoxType(),
                letterSummary.getCreatedAt());
    }

    public static List<LetterSummaryResponse> fromList(List<LetterSummary> letterSummaries) {
        return letterSummaries.stream().map(LetterSummaryResponse::from).toList();
    }
}
