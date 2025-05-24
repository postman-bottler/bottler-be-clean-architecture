package online.bottler.mapletter.adaptor.in.web.request;

import java.util.List;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;

public record DeleteMapLettersRequest(
        List<LetterInfo> letters
) {
    public record LetterInfo(
            DeleteLetterType letterType,
            Long letterId
    ) {
    }

    public DeleteMapLettersCommand toCommand() {
        return new DeleteMapLettersCommand(
                letters.stream()
                        .map(letter -> new DeleteMapLettersCommand.LetterInfo(
                                DeleteMapLettersCommand.LetterType.valueOf(letter.letterType().name()),
                                letter.letterId()))
                        .toList());
    }
}
