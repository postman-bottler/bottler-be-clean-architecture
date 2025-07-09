package online.bottler.letter.domain;

public record RecommendedLetter(Long id, Long userId, Long letterId) {

    public static RecommendedLetter of(Long id, Long userId, Long letterId) {
        return new RecommendedLetter(id, userId, letterId);
    }

    public static RecommendedLetter create(Long userId, Long letterId) {
        return new RecommendedLetter(null, userId, letterId);
    }
}
