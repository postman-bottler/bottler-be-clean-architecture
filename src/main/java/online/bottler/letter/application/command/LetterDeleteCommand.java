package online.bottler.letter.application.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterDeleteCommand(Long letterId, LetterType letterType, BoxType boxType) {
    public static Map<LetterType, Map<BoxType, List<Long>>> groupByTypeAndBox(List<LetterDeleteCommand> commands) {
        return commands.stream()
                .collect(Collectors.groupingBy(
                        LetterDeleteCommand::letterType,
                        Collectors.groupingBy(
                                LetterDeleteCommand::boxType,
                                Collectors.mapping(LetterDeleteCommand::letterId, Collectors.toList())
                        )
                ));
    }
}
