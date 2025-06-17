package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import online.bottler.letter.application.command.LetterDeleteCommand;

public record LetterDeleteRequest(@NotNull(message = "Letter ID는 필수입니다.") Long letterId,
                                  @NotNull(message = "Letter Type은 필수입니다.") String letterType,
                                  @NotNull(message = "Box Type은 필수입니다.") String boxType) {

    public LetterDeleteCommand toCommand() {
        return LetterDeleteCommand.of(letterId, letterType, boxType);
    }

    public static List<LetterDeleteCommand> toCommandList(List<LetterDeleteRequest> letterDeleteRequests) {
        return letterDeleteRequests.stream().map(LetterDeleteRequest::toCommand).toList();
    }
}
