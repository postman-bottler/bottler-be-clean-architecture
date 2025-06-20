package online.bottler.letter.application.command;

import static online.bottler.letter.domain.BoxType.NONE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterDeleteKey;
import online.bottler.letter.domain.LetterDeleteValues;
import online.bottler.letter.domain.LetterType;

public record LetterDeleteCommand(Long letterId, LetterType letterType, BoxType boxType) {
    public static LetterDeleteCommand of(Long letterId, String letterType, String boxType) {
        return new LetterDeleteCommand(letterId, LetterType.valueOf(letterType), BoxType.valueOf(boxType));
    }

    public static Map<LetterDeleteKey, LetterDeleteValues> toLetterDeleteMap(List<LetterDeleteCommand> letterDeleteCommands) {
        Map<LetterDeleteKey, LetterDeleteValues> groupedLetters = new HashMap<>();

        letterDeleteCommands.stream()
                .filter(command -> command.letterType() != LetterType.NONE && command.boxType() != NONE)
                .forEach(command -> {
                    LetterDeleteKey key = LetterDeleteKey.of(command.letterType(), command.boxType());
                    groupedLetters.computeIfAbsent(key, k -> new LetterDeleteValues(new ArrayList<>()))
                            .letterIds().add(command.letterId());
                });

        return groupedLetters;
    }
}
