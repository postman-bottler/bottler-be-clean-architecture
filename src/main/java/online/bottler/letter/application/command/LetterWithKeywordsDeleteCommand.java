package online.bottler.letter.application.command;

import online.bottler.letter.domain.BoxType;

public record LetterWithKeywordsDeleteCommand(Long userId, Long letterId, BoxType boxType) {
    public static LetterWithKeywordsDeleteCommand of(Long userId, Long letterId, String boxType) {
        return new LetterWithKeywordsDeleteCommand(userId, letterId, BoxType.valueOf(boxType));
    }
}
