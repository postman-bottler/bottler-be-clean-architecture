package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public record LetterDeleteRequest(@NotNull(message = "Letter ID는 필수입니다.") Long letterId,
                                  @NotNull(message = "Letter Type은 필수입니다.") LetterType letterType,
                                  @NotNull(message = "Box Type은 필수입니다.") BoxType boxType) {

    public LetterDeleteCommand toCommand() {
        return new LetterDeleteCommand(letterId, letterType, boxType);
    }

    public static List<LetterDeleteCommand> toCommandList(List<LetterDeleteRequest> letterDeleteRequests) {
        return letterDeleteRequests.stream().map(LetterDeleteRequest::toCommand).toList();
    }
}
