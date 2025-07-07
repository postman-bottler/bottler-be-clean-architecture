package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;

public record ReplyLetterDeleteRequest(@NotBlank(message = "Letter ID는 필수입니다.") Long letterId,
                                       @NotBlank(message = "Box Type은 필수입니다.") String boxType) {
    public ReplyLetterDeleteCommand toCommand(Long userId) {
        return ReplyLetterDeleteCommand.of(userId, letterId, boxType);
    }
}
