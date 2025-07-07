package online.bottler.letter.application.command;

public record LetterWithKeywordsDetailQuery(Long userId, Long letterId) {
    public static LetterWithKeywordsDetailQuery of(Long userId, Long letterId) {
        return new LetterWithKeywordsDetailQuery(userId, letterId);
    }
}
