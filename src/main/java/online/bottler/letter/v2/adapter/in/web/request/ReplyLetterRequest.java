package online.bottler.letter.v2.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import online.bottler.letter.domain.LetterContent;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.v2.application.command.ReplyLetterCommand;

public record ReplyLetterRequest(@NotBlank(message = "편지 내용은 필수입니다.") String content,
                                 @NotBlank(message = "글씨체는 필수입니다.") String font,
                                 @NotBlank(message = "편지지는 필수입니다.") String paper,
                                 @NotBlank(message = "라벨은 필수입니다.") String label) {
    public ReplyLetter toDomain(String title, Long letterId, Long receiverId, Long senderId) {
        return ReplyLetter.create(senderId, LetterContent.of(title, content, font, paper, label), letterId, receiverId);
    }

    public ReplyLetterCommand toCommand(Long letterId, Long userId) {
        return ReplyLetterCommand.of(letterId, userId, LetterContent.of(null, content, font, paper, label));
    }
}
