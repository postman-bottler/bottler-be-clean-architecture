package online.bottler.letter.application.dto;

import online.bottler.letter.domain.RecommendedLetter;

public record RecommendedLetterDTO(Long userId, Long letterId) {
    public static RecommendedLetterDTO of(Long userId, Long letterId) {
        return new RecommendedLetterDTO(userId, letterId);
    }

    public RecommendedLetter toDomain() {
        return RecommendedLetter.builder()
                .userId(userId)
                .letterId(letterId)
                .build();
    }
}
