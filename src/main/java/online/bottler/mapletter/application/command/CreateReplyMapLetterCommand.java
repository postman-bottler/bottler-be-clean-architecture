package online.bottler.mapletter.application.command;

import java.time.LocalDateTime;
import online.bottler.mapletter.domain.ReplyMapLetter;

public record CreateReplyMapLetterCommand(
        Long sourceLetter,
        String content,
        String font,
        String paper,
        String label
) {
    public static ReplyMapLetter toReplyMapLetter(CreateReplyMapLetterCommand command, Long userId) {
        return ReplyMapLetter.builder()
                .sourceLetterId(command.sourceLetter)
                .font(command.font)
                .content(command.content)
                .paper(command.paper)
                .label(command.label)
                .isBlocked(false)
                .isDeleted(false)
                .createUserId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isRecipientDeleted(false)
                .build();
    }
}
