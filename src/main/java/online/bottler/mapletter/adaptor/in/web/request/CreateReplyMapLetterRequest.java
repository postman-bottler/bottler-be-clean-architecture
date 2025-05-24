package online.bottler.mapletter.adaptor.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;

public record CreateReplyMapLetterRequest(
        @NotNull(message = "원본 편지는 생략이 불가능합니다.") Long sourceLetter,
        @NotBlank(message = "편지 내용은 생략이 불가능합니다.") String content,
        String font,
        String paper,
        String label
) {
    public CreateReplyMapLetterCommand toCommand() {
        return new CreateReplyMapLetterCommand(sourceLetter(), content(), font(), paper(), label());
    }
}
