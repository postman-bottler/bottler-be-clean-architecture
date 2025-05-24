package online.bottler.letter.v2.application.command;

import online.bottler.letter.domain.BoxType;

public record LetterWithKeywordsDeleteCommand(Long letterId, Long userId, BoxType boxType) {
    public static LetterWithKeywordsDeleteCommand of(Long letterId, Long userId, BoxType boxType) {
        return new LetterWithKeywordsDeleteCommand(letterId, userId, boxType);
    }
}
