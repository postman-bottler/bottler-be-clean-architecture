package online.bottler.letter.application.command;

import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterDeleteCommand(Long letterId, LetterType letterType, BoxType boxType) {
    public static LetterDeleteCommand of(Long letterId, String letterType, String boxType) {
        return new LetterDeleteCommand(letterId, LetterType.valueOf(letterType), BoxType.valueOf(boxType));
    }
}
