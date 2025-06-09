package online.bottler.letter.application.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterDeleteKey;
import online.bottler.letter.domain.LetterDeleteValues;
import online.bottler.letter.domain.LetterType;

public record LetterDeleteCommand(Long letterId, LetterType letterType, BoxType boxType) {
    public static Map<LetterDeleteKey, LetterDeleteValues> groupByLetterTypeAndBoxType(List<LetterDeleteCommand> commands) {
        Map<LetterDeleteKey, LetterDeleteValues> grouped = new HashMap<>();

        for (LetterType letterType : LetterType.values()) {
            if (letterType == LetterType.NONE) continue;

            for (BoxType boxType : BoxType.values()) {
                if (boxType == BoxType.NONE) continue;

                List<Long> filteredIds = commands.stream()
                        .filter(command -> command.letterType() == letterType && command.boxType() == boxType)
                        .map(LetterDeleteCommand::letterId)
                        .collect(Collectors.toList());

                if (!filteredIds.isEmpty()) {
                    LetterDeleteKey key = new LetterDeleteKey(letterType, boxType);
                    grouped.put(key, new LetterDeleteValues(filteredIds));
                }
            }
        }

        return grouped;
    }


}
