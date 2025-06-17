package online.bottler.letter.application.command;

import online.bottler.letter.domain.BoxType;

public record LetterWithKeywordsDeleteCommand(Long letterId, Long userId, BoxType boxType) {
    public static LetterWithKeywordsDeleteCommand of(Long letterId, Long userId, String boxType) {
        return new LetterWithKeywordsDeleteCommand(letterId, userId, BoxType.valueOf(boxType));
    }
}
