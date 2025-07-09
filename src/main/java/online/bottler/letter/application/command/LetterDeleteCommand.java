package online.bottler.letter.application.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterDeletion;

public record LetterDeleteCommand(Long letterId, LetterBoxType letterBoxType) {
    public static LetterDeleteCommand of(Long letterId, String letterType, String boxType) {
        return new LetterDeleteCommand(letterId, LetterBoxType.from(letterType, boxType));
    }

    public static Map<LetterBoxType, LetterDeletion> toLetterDeleteMap(List<LetterDeleteCommand> letterDeleteCommands) {
        return letterDeleteCommands.stream()
                .filter(cmd -> cmd.letterBoxType.isValid())
                .collect(
                        Collectors.toMap(
                                cmd -> cmd.letterBoxType,
                                cmd -> new LetterDeletion(List.of(cmd.letterId())),
                                (existing, replacement) -> {
                                    existing.letterIds().addAll(replacement.letterIds());
                                    return existing;
                                }
                        )
                );
    }
}
