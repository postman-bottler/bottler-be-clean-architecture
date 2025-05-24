package online.bottler.letter.v2.application.command;

public record LetterWithKeywordsDetailQuery(
        Long letterId,
        Long userId
) {
    public static LetterWithKeywordsDetailQuery of(Long letterId, Long userId) {
        return new LetterWithKeywordsDetailQuery(letterId, userId);
    }
}
