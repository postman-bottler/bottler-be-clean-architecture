package online.bottler.letter.application.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterDeletion;

public record LetterDeleteCommand(Long letterId, LetterBoxType letterBoxType) {
    public static LetterDeleteCommand of(Long letterId, String letterType, String boxType) {
        return new LetterDeleteCommand(letterId, LetterBoxType.from(letterType, boxType));
    }

    public static Map<LetterBoxType, LetterDeletion> toLetterDeleteMap(List<LetterDeleteCommand> letterDeleteCommands) {
        Map<LetterBoxType, LetterDeletion> groupedLetters = new HashMap<>();

        letterDeleteCommands.stream()
                .filter(command -> command.letterBoxType.isValid())
                .forEach(command ->
                        groupedLetters
                        .computeIfAbsent(
                                command.letterBoxType, k -> new LetterDeletion(new ArrayList<>())
                        )
                        .letterIds()
                                .add(command.letterId())
                );

        return groupedLetters;
    }
}
