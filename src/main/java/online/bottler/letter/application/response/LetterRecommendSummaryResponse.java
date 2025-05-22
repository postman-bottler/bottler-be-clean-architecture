package online.bottler.letter.application.response;

import online.bottler.letter.domain.Letter;

public record LetterRecommendSummaryResponse(
        Long letterId,
        String title,
        String label
) {
    public static LetterRecommendSummaryResponse from(Letter letter) {
        return new LetterRecommendSummaryResponse(
                letter.getId(),
                letter.getTitle(),
                letter.getLabel()
        );
    }
}
