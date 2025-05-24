package online.bottler.letter.v2.application.response;

import java.util.List;
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

    public static List<LetterRecommendSummaryResponse> fromList(List<Letter> letters) {
        return letters.stream().map(LetterRecommendSummaryResponse::from).toList();
    }
}
