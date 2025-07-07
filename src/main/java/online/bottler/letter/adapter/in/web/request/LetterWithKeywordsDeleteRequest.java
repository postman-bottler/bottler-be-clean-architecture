package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;

public record LetterWithKeywordsDeleteRequest(@NotNull(message = "Letter ID는 필수입니다.") Long letterId,
                                              @NotBlank(message = "Box Type은 필수입니다.") String boxType) {
    public LetterWithKeywordsDeleteCommand toCommand(Long userId) {
        return LetterWithKeywordsDeleteCommand.of(userId, letterId, boxType);
    }
}
