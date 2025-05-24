package online.bottler.letter.application.command;

import online.bottler.letter.domain.RecommendedLetter;

public record RecommendedLetterDTO(Long userId, Long letterId) {
    public static RecommendedLetterDTO of(Long userId, Long letterId) {
        return new RecommendedLetterDTO(userId, letterId);
    }

    public RecommendedLetter toDomain() {
        return RecommendedLetter.create(userId, letterId);
    }
}
