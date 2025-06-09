package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.domain.LetterContent;

public record ReplyLetterRequest(@NotBlank(message = "편지 내용은 필수입니다.") String content,
                                 @NotBlank(message = "글씨체는 필수입니다.") String font,
                                 @NotBlank(message = "편지지는 필수입니다.") String paper,
                                 @NotBlank(message = "라벨은 필수입니다.") String label) {
    public ReplyLetterCommand toCommand(Long letterId, Long userId) {
        return ReplyLetterCommand.of(letterId, userId, LetterContent.of(null, content, font, paper, label));
    }
}
