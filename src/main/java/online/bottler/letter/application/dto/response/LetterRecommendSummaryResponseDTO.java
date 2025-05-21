package online.bottler.letter.application.dto.response;

import online.bottler.letter.domain.Letter;

public record LetterRecommendSummaryResponseDTO(
        Long letterId,
        String title,
        String label
) {
    public static LetterRecommendSummaryResponseDTO from(Letter letter) {
        return new LetterRecommendSummaryResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getLabel()
        );
    }
}
